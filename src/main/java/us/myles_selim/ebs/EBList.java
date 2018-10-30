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

import us.myles_selim.ebs.callbacks.OnWriteCallback;

public class EBList<W, T extends DataType<W>> extends ArrayList<T> {

	private static final long serialVersionUID = -5713554517110763629L;
	private final T type;

	public EBList(T type) {
		this.type = type;
	}

	public boolean addWrapped(W wrapped) {
		T inst;
		try {
			@SuppressWarnings("unchecked")
			Constructor<T> construct = (Constructor<T>) type.getClass().getConstructor();
			inst = construct.newInstance();
			inst.setValue(wrapped);
		} catch (NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return false;
		}
		return this.add(inst);
	}

	@Override
	public T set(int index, T element) {
		T val = super.set(index, element);
		this.callOnWrite();
		return val;
	}

	@Override
	public boolean add(T e) {
		boolean val = super.add(e);
		this.callOnWrite();
		return val;
	}

	@Override
	public void add(int index, T element) {
		super.add(index, element);
		this.callOnWrite();
	}

	@Override
	public T remove(int index) {
		T val = super.remove(index);
		this.callOnWrite();
		return val;
	}

	@Override
	public boolean remove(Object o) {
		boolean val = super.remove(o);
		this.callOnWrite();
		return val;
	}

	@Override
	public void clear() {
		super.clear();
		this.callOnWrite();
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		boolean val = super.addAll(c);
		this.callOnWrite();
		return val;
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
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
	public boolean removeIf(Predicate<? super T> filter) {
		boolean val = super.removeIf(filter);
		this.callOnWrite();
		return val;
	}

	@Override
	public void replaceAll(UnaryOperator<T> operator) {
		super.replaceAll(operator);
		this.callOnWrite();
	}

	@Override
	public void sort(Comparator<? super T> c) {
		super.sort(c);
		this.callOnWrite();
	}

	@Override
	public boolean contains(Object o) {
		return super.contains(o) || containsWrapped(o);
	}

	public boolean containsWrapped(Object o) {
		for (T t : this)
			if (t == o || (t != null && t.getValue().equals(o)))
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
		for (T t : this)
			vals.add(t.getValue());
		return Collections.unmodifiableList(vals);
	}

	public byte[] serialize() {
		Storage storage = new Storage();
		storage.writeString(type.getClass().getName());
		storage.writeInt(this.size());
		for (T t : this) {
			storage.setLengthMarker();
			t.toBytes(storage);
			storage.writeLengthMarker();
		}
		return storage.getAsByteArray();
	}

	private OnWriteCallback onWriteCallback;

	private void callOnWrite() {
		if (this.onWriteCallback != null)
			this.onWriteCallback.onWrite();
	}

	public EBList<W, T> setOnWriteCallback(OnWriteCallback onWrite) {
		this.onWriteCallback = onWrite;
		return this;
	}

	public static <W, T extends DataType<W>> EBList<W, T> deserialize(byte[] data) {
		Storage storage = new Storage(data);
		String typeName = storage.readString();
		int numValues = storage.readInt();
		try {
			@SuppressWarnings("unchecked")
			Class<T> type = (Class<T>) Class.forName(typeName);
			Constructor<T> construct = type.getConstructor();
			T typeInstance = construct.newInstance();
			EBList<W, T> list = new EBList<>(typeInstance);
			for (int i = 0; i < numValues; i++) {
				if (storage.getVersion() > 0) {
					int length = storage.readInt();
					storage.setReadDistance(length);
				}
				T newInstance = construct.newInstance();
				newInstance.fromBytes(storage);
				if (storage.getVersion() > 0)
					storage.clearReadDistance();
				list.add(newInstance);
			}
			return list;
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException
				| InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}

}
