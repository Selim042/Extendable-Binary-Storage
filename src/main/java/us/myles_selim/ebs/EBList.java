package us.myles_selim.ebs;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import us.myles_selim.ebs.callbacks.ClassNotFoundCallback;
import us.myles_selim.ebs.callbacks.OnWriteCallback;

public class EBList<W> extends ArrayList<DataType<W>> {

	private static final long serialVersionUID = -5713554517110763629L;
	private final DataType<W> type;

	public EBList(DataType<W> type) {
		this.type = type;
	}

	public EBList(EBList<W> ebl) {
		super(ebl);
		this.type = ebl.type;
	}

	public boolean addWrapped(W wrapped) {
		DataType<W> inst;
		try {
			@SuppressWarnings("unchecked")
			Constructor<DataType<W>> construct = (Constructor<DataType<W>>) type.getClass()
					.getConstructor();
			inst = construct.newInstance();
			inst.setValue(wrapped);
		} catch (NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return false;
		}
		return this.add(inst);
	}

	@Override
	public DataType<W> set(int index, DataType<W> element) {
		DataType<W> val = super.set(index, element);
		this.callOnWrite();
		return val;
	}

	@Override
	public boolean add(DataType<W> e) {
		boolean val = super.add(e);
		this.callOnWrite();
		return val;
	}

	@Override
	public void add(int index, DataType<W> element) {
		super.add(index, element);
		this.callOnWrite();
	}

	@Override
	public DataType<W> remove(int index) {
		DataType<W> val = super.remove(index);
		this.callOnWrite();
		return val;
	}

	@Override
	public boolean remove(Object o) {
		boolean val = super.remove(o);
		this.callOnWrite();
		return val;
	}

	public boolean removeWrapped(Object o) {
		for (DataType<?> d : this)
			if (d != null && d.getValue().equals(o)) {
				this.remove(d);
				return true;
			}
		return false;
	}

	@Override
	public void clear() {
		super.clear();
		this.callOnWrite();
	}

	@Override
	public boolean addAll(Collection<? extends DataType<W>> c) {
		boolean val = super.addAll(c);
		this.callOnWrite();
		return val;
	}

	@Override
	public boolean addAll(int index, Collection<? extends DataType<W>> c) {
		boolean val = super.addAll(index, c);
		this.callOnWrite();
		return val;
	}

	@Override
	protected void removeRange(int fromIndex, int toIndex) {
		super.removeRange(fromIndex, toIndex);
		this.callOnWrite();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean val = super.removeAll(c);
		this.callOnWrite();
		return val;
	}

	@Override
	public boolean removeIf(Predicate<? super DataType<W>> filter) {
		boolean val = super.removeIf(filter);
		this.callOnWrite();
		return val;
	}

	@Override
	public void replaceAll(UnaryOperator<DataType<W>> operator) {
		super.replaceAll(operator);
		this.callOnWrite();
	}

	@Override
	public void sort(Comparator<? super DataType<W>> c) {
		super.sort(c);
		this.callOnWrite();
	}

	@Override
	public boolean contains(Object o) {
		return super.contains(o) || containsWrapped(o);
	}

	public boolean containsWrapped(Object o) {
		for (DataType<W> t : this)
			if (t != null && t.getValue().equals(o))
				return true;
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object o : c)
			if (!contains(o) && !containsWrapped(o))
				return false;
		return true;
	}

	public boolean containsAllWrapped(Collection<?> c) {
		for (Object o : c)
			if (!containsWrapped(o))
				return false;
		return true;
	}

	public List<W> values() {
		List<W> vals = new LinkedList<>();
		for (DataType<W> t : this)
			vals.add(t.getValue());
		return Collections.unmodifiableList(vals);
	}

	public byte[] serialize() {
		Storage storage = new Storage();
		storage.writeString(type.getClass().getName());
		storage.writeInt(this.size());
		for (DataType<W> t : this) {
			storage.setLengthMarker();
			t.toBytes(storage);
			storage.writeLengthMarker();
		}
		return storage.getAsByteArray();
	}

	private OnWriteCallback onWriteCallback;

	private void callOnWrite() {
		if (this.onWriteCallback != null)
			this.onWriteCallback.onWriteEBL(this);
	}

	public EBList<W> setOnWriteCallback(OnWriteCallback onWrite) {
		this.onWriteCallback = onWrite;
		return this;
	}

	public void markDirty() {
		callOnWrite();
	}

	public static <W> EBList<W> deserialize(byte[] data) {
		return deserialize(data, null);
	}

	@SuppressWarnings("unchecked")
	public static <W> EBList<W> deserialize(byte[] data, ClassNotFoundCallback classNotFound) {
		Storage storage = new Storage(data);
		String typeName = storage.readString();
		int numValues = storage.readInt();
		Class<DataType<W>> type;
		try {
			type = (Class<DataType<W>>) Class.forName(typeName);
		} catch (ClassNotFoundException e) {
			try {
				if (classNotFound == null)
					throw new ClassNotFoundException();
				type = (Class<DataType<W>>) Class.forName(classNotFound.getNewPath(typeName));
			} catch (ClassNotFoundException e2) {
				e.printStackTrace();
				return null;
			}
		}
		try {
			Constructor<DataType<W>> construct = type.getConstructor();
			DataType<W> typeInstance = construct.newInstance();
			EBList<W> list = new EBList<>(typeInstance);
			for (int i = 0; i < numValues; i++) {
				if (storage.getVersion() > 0) {
					int length = storage.readInt();
					storage.setReadDistance(length);
				}
				DataType<W> newInstance = construct.newInstance();
				newInstance.fromBytes(storage);
				if (storage.getVersion() > 0)
					storage.clearReadDistance();
				list.add(newInstance);
			}
			return list;
		} catch (NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}

}
