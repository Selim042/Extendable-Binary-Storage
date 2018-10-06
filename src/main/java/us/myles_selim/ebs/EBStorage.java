package us.myles_selim.ebs;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class EBStorage {

	private final Map<Integer, DataType<?>> dataTypes = new HashMap<>();
	private final Map<String, DataType<?>> data = new ConcurrentHashMap<>();
	private int next = 0;

	public EBStorage() {}

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
		return this;
	}

	private EBStorage registerType(int id, DataType<?> dataType) {
		try {
			dataType.getClass().getConstructor();
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException(
					"data types must have a public default constructor: " + dataType.getClass());
		}
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

	public void set(String name, Object value) {
		if (acceptsValue(value))
			data.put(name, getDataType(value));
	}

	public Object get(String name) {
		if (data.containsKey(name))
			return data.get(name).getValue();
		return null;
	}

	public <T> T get(String name, Class<T> type) {
		if (data.containsKey(name))
			return type.cast(data.get(name).getValue());
		return null;
	}

	public boolean acceptsValue(Object obj) {
		for (Entry<Integer, DataType<?>> e : dataTypes.entrySet())
			if (e.getValue().acceptsValue(obj))
				return true;
		return false;
	}

	public byte[] serialize() {
		Storage storage = new Storage();
		storage.writeInt(dataTypes.size());
		for (Entry<Integer, DataType<?>> e : dataTypes.entrySet()) {
			storage.writeInt(e.getKey());
			storage.writeString(e.getValue().getClass().getName());
		}
		storage.writeInt(data.size());
		for (Entry<String, DataType<?>> e : data.entrySet()) {
			int id = getTypeId(e.getValue());
			if (id == -1)
				return null;
			storage.writeInt(id);
			storage.writeString(e.getKey());
			e.getValue().toBytes(storage);
		}
		return storage.getByteArray();
	}

	public static EBStorage deserialize(byte[] data) {
		if (data == null)
			return new EBStorage();
		boolean valid = true;
		EBStorage ebs = new EBStorage();
		Storage storage = new Storage(data);
		int numTypes = storage.readInt();
		for (int i = 0; i < numTypes; i++) {
			try {
				int id = storage.readInt();
				Class<?> clazz = Class.forName(storage.readString());
				Constructor<?> construct = clazz.getConstructor();
				ebs.registerType(id, (DataType<?>) construct.newInstance());
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException
					| InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				valid = false;
				break;
			}
		}
		int numData = storage.readInt();
		for (int i = 0; i < numData; i++) {
			int id = storage.readInt();
			DataType<?> newType = getNewDataType(ebs.getType(id));
			if (newType == null) {
				valid = false;
				break;
			}
			ebs.data.put(storage.readString(), newType);
			newType.fromBytes(storage);
		}
		return valid ? ebs : null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private <T> DataType<T> getDataType(T val) {
		for (Entry<Integer, DataType<?>> e : dataTypes.entrySet()) {
			if (e.getValue().acceptsValue(val)) {
				try {
					Constructor<? extends DataType> construct = e.getValue().getClass().getConstructor();
					DataType<?> newData = construct.newInstance();
					newData.setValueObject(val);
					return (DataType<T>) newData;
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e1) {
					e1.printStackTrace();
				}
			}
		}
		return null;
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

	public static void main(String... args) {
		EBStorage storage = new EBStorage().registerPrimitives();
		// storage.set("testString", "Hello, World");
		// System.out.println(storage.get("testString"));
		// System.out.println((String) storage.get("testString"));
		// System.out.println(storage.get("testString", String.class));

		// try {
		// byte[] serialized = storage.serialize();
		// FileOutputStream out = new FileOutputStream("testFile.ebs");
		// out.write(serialized);
		// out.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		storage.set("testInt", 5);
		storage.set("string", "bleh");
		storage.set("testDouble", 123.455);
		byte[] serialized = storage.serialize();
		EBStorage storage2 = EBStorage.deserialize(serialized);
		System.out.println(storage2.get("testInt"));
		System.out.println(storage2.get("string"));
		System.out.println(storage2.get("testDouble"));
	}

}
