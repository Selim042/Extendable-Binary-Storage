package us.myles_selim.ebs;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import us.myles_selim.ebs.callbacks.ClassNotFoundCallback;
import us.myles_selim.ebs.callbacks.OnWriteCallback;
import us.myles_selim.ebs.callbacks.ParentFlushCallback;
import us.myles_selim.ebs.data_types.DataTypeBoolean;
import us.myles_selim.ebs.data_types.DataTypeByte;
import us.myles_selim.ebs.data_types.DataTypeByteArray;
import us.myles_selim.ebs.data_types.DataTypeCharacter;
import us.myles_selim.ebs.data_types.DataTypeDouble;
import us.myles_selim.ebs.data_types.DataTypeEBList;
import us.myles_selim.ebs.data_types.DataTypeEBStorage;
import us.myles_selim.ebs.data_types.DataTypeFloat;
import us.myles_selim.ebs.data_types.DataTypeIntArray;
import us.myles_selim.ebs.data_types.DataTypeInteger;
import us.myles_selim.ebs.data_types.DataTypeLong;
import us.myles_selim.ebs.data_types.DataTypeShort;
import us.myles_selim.ebs.data_types.DataTypeString;

public class EBStorage implements IDataTypeHolder<EBStorage> {

	private final Map<Integer, DataType<?>> dataTypes;
	private final Map<String, DataType<?>> data;
	private int next;

	public EBStorage() {
		this.dataTypes = new HashMap<>();
		this.data = new ConcurrentHashMap<>();
		this.next = 0;
	}

	public EBStorage(EBStorage ebs) {
		this.dataTypes = new HashMap<>(ebs.dataTypes);
		this.data = new ConcurrentHashMap<>(ebs.data);
		this.next = ebs.next;
	}

	public EBStorage registerPrimitives() {
		registerType(new DataTypeBoolean()); // 0
		registerType(new DataTypeByte()); // 1
		registerType(new DataTypeCharacter()); // 2
		registerType(new DataTypeDouble()); // 3
		registerType(new DataTypeFloat()); // 4
		registerType(new DataTypeInteger()); // 5
		registerType(new DataTypeLong()); // 6
		registerType(new DataTypeShort()); // 7
		registerType(new DataTypeString()); // 8
		registerType(new DataTypeByteArray()); // 9
		registerType(new DataTypeEBStorage()); // 10
		registerType(new DataTypeIntArray()); // 11
		registerType(new DataTypeEBList()); // 12
		return this;
	}

	private EBStorage registerType(int id, DataType<?> dataType) {
		try {
			dataType.getClass().getConstructor();
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException(
					"data types must have a public default constructor: " + dataType.getClass());
		}
		for (Entry<Integer, DataType<?>> e : dataTypes.entrySet())
			if (e.getValue().getClass().equals(dataType.getClass()))
				return this;
		if (dataTypes.containsKey(id))
			throw new IllegalArgumentException("data type with id " + id + " is already registered: "
					+ dataTypes.get(id).getClass().getName());
		dataTypes.put(id, dataType);
		return this;
	}

	public EBStorage registerType(DataType<?> dataType) {
		int id = getUnusedInt();
		return registerType(id, dataType);
	}

	public <T> int getTypeId(DataType<T> data) {
		for (Entry<Integer, DataType<?>> e : dataTypes.entrySet())
			if (e.getValue().getClass().equals(data.getClass()))
				return e.getKey();
		return -1;
	}

	public DataType<?> getType(int id) {
		return dataTypes.get(id);
	}

	private int getUnusedInt() {
		while (dataTypes.containsKey(next))
			next++;
		return next;
	}

	public void set(String name, Object value) throws IllegalArgumentException {
		if (acceptsValue(value)) {
			data.put(name, getDataType(value));
			callOnWrite();
		} else
			throw new IllegalArgumentException(
					"DataType for " + value + "(" + value.getClass().getName() + ") not registered");
	}

	public void clearKey(String name) {
		data.remove(name);
		callOnWrite();
	}

	public Object get(String name) {
		if (data.containsKey(name))
			return data.get(name).getValue();
		return null;
	}

	public <T> T get(String name, Class<T> type) {
		return type.cast(get(name));
	}

	public Collection<String> getKeys() {
		return Collections.unmodifiableCollection(this.data.keySet());
	}

	public boolean containsKey(String key) {
		return this.data.containsKey(key);
	}

	public boolean acceptsValue(Object obj) {
		for (Entry<Integer, DataType<?>> e : dataTypes.entrySet())
			if (e.getValue().acceptsValue(obj))
				return true;
		return false;
	}

	@Override
	public byte[] serialize() {
		Storage storage = new Storage();
		storage.writeInt(dataTypes.size());
		for (Entry<Integer, DataType<?>> e : dataTypes.entrySet()) {
			storage.writeInt(e.getKey());
			storage.writeString(e.getValue().getClass().getName());
		}
		storage.writeInt(data.size());
		for (Entry<String, DataType<?>> e : data.entrySet()) {
			storage.setLengthMarker();
			int id = getTypeId(e.getValue());
			if (id == -1)
				return null;
			storage.writeInt(id);
			storage.writeString(e.getKey());
			e.getValue().toBytes(storage);
			storage.writeLengthMarker();
		}
		return storage.getAsByteArray();
	}

	private OnWriteCallback onWriteCallback;

	private void callOnWrite() {
		if (this.onWriteCallback != null)
			this.onWriteCallback.onWriteEBS(this);
	}

	@Override
	public EBStorage setOnWriteCallback(OnWriteCallback onWrite) {
		this.onWriteCallback = onWrite;
		return this;
	}

	@Override
	public void flush() {
		callOnWrite();
	}

	public static EBStorage deserialize(byte[] data) {
		return deserialize(data, null);
	}

	public static EBStorage deserialize(byte[] data, ClassNotFoundCallback classNotFound) {
		if (data == null)
			return new EBStorage();
		boolean valid = true;
		EBStorage ebs = new EBStorage();
		Storage storage = new Storage(data);
		List<Integer> typesToDelete = new ArrayList<>();
		int numTypes = storage.readInt();
		for (int i = 0; i < numTypes; i++) {
			int id = storage.readInt();
			String className = storage.readString();
			Class<?> clazz;
			try {
				clazz = Class.forName(className);
			} catch (ClassNotFoundException e) {
				try {
					if (classNotFound == null)
						throw new ClassNotFoundException();
					if (classNotFound.shouldJustDelete(className)) {
						typesToDelete.add(id);
						continue;
					}
					clazz = Class.forName(classNotFound.getNewPath(className));
				} catch (ClassNotFoundException e2) {
					e.printStackTrace();
					valid = false;
					break;
				}
			}
			try {
				Constructor<?> construct = clazz.getConstructor();
				ebs.registerType(id, (DataType<?>) construct.newInstance());
			} catch (NoSuchMethodException | SecurityException | InstantiationException
					| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
				valid = false;
				break;
			}
		}
		int numData = storage.readInt();
		for (int i = 0; i < numData; i++) {
			if (storage.getVersion() > 0) {
				int length = storage.readInt();
				storage.setReadDistance(length);
			}
			int id = storage.readInt();
			if (typesToDelete.contains(id))
				continue;
			DataType<?> newType = getNewDataType(ebs.getType(id));
			if (newType == null) {
				valid = false;
				break;
			}
			ebs.data.put(storage.readString(), newType);
			newType.fromBytes(storage);
			if (storage.getVersion() > 0)
				storage.clearReadDistance();
			if (newType instanceof DataTypeEBStorage) {
				DataTypeEBStorage type = (DataTypeEBStorage) newType;
				if (type.getValue() != null)
					type.getValue().setOnWriteCallback(new ParentFlushCallback(ebs));
			}
			newType.setParent(ebs);
		}
		return valid ? ebs : null;
	}

	@SuppressWarnings({ "unchecked" })
	private <T> DataType<T> getDataType(T val) {
		for (Entry<Integer, DataType<?>> e : dataTypes.entrySet()) {
			if (e.getValue().acceptsValue(val)) {
				try {
					DataType<?> newData = e.getValue().getClass().getConstructor().newInstance();
					newData.setValueObject(val);
					newData.setParent(this);
					return (DataType<T>) newData;
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e1) {
					e1.printStackTrace();
				}
			}
		}
		throw new IllegalArgumentException("DataType for " + val.getClass().getName() + " not found");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static <T> DataType<T> getNewDataType(DataType<T> type) {
		if (type == null)
			return null;
		try {
			Constructor<? extends DataType> construct = type.getClass().getConstructor();
			return construct.newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e1) {
			e1.printStackTrace();
		}
		return null;
	}

}
