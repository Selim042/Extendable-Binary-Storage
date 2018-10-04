package us.myles_selim.ebs;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class EBStorage {

	private final Map<Integer, DataType<?>> dataTypes = new HashMap<>();
	private final Map<String, Object> data = new ConcurrentHashMap<>();
	private int next = 0;

	public EBStorage() {
		int simpleId = 0;
		registerType(simpleId++, new DataTypeBoolean()); // 0
		registerType(simpleId++, new DataTypeByte()); // 1
		registerType(simpleId++, new DataTypeCharacter()); // 2
		registerType(simpleId++, new DataTypeDouble()); // 3
		registerType(simpleId++, new DataTypeFloat());// 4
		registerType(simpleId++, new DataTypeInteger());// 5
		registerType(simpleId++, new DataTypeLong());// 6
		registerType(simpleId++, new DataTypeShort());// 7
		registerType(simpleId++, new DataTypeString());// 8
	}

	private EBStorage registerType(int id, DataType<?> dataType) {
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

	private int getUnusedInt() {
		while (dataTypes.containsKey(next))
			next++;
		return next;
	}

	public void set(String name, Object value) {
		if (acceptsValue(value))
			data.put(name, value);
	}

	public Object get(String name) {
		if (data.containsKey(name))
			return data.get(name);
		return null;
	}

	public <T> T get(String name, Class<T> type) {
		if (data.containsKey(name))
			return type.cast(data.get(name));
		return null;
	}

	public boolean acceptsValue(Object obj) {
		for (Entry<Integer, DataType<?>> e : dataTypes.entrySet())
			if (e.getValue().acceptsValue(obj))
				return true;
		return false;
	}

	public static void main(String... args) {
		EBStorage storage = new EBStorage();
		storage.set("testString", "Hello, World");
		System.out.println(storage.get("testString"));
		System.out.println((String) storage.get("testString"));
		System.out.println(storage.get("testString", String.class));
	}

}
