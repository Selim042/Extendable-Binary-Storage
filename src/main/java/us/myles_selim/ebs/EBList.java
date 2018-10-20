package us.myles_selim.ebs;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
		} catch (NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return false;
		}
		inst.setValue(wrapped);
		return this.add(inst);
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
		for (T t : this)
			t.toBytes(storage);
		return storage.getAsByteArray();
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
				T newInstance = construct.newInstance();
				newInstance.fromBytes(storage);
				list.add(newInstance);
			}
			return list;
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException
				| InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			return null;
		}
	}

}
