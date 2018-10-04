package us.myles_selim.ebs;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class EBStorage {

	private final Map<Integer, DataType<?>> dataTypes = new ConcurrentHashMap<>();

	private final Map<String, Object> data = new ConcurrentHashMap<>();

	public EBStorage() {
		int simpleId = 0;
		registerType(simpleId++, new DataTypeBoolean());
		registerType(simpleId++, new DataTypeByte());
		registerType(simpleId++, new DataTypeCharacter());
		registerType(simpleId++, new DataTypeDouble());
		registerType(simpleId++, new DataTypeFloat());
		registerType(simpleId++, new DataTypeInteger());
		registerType(simpleId++, new DataTypeLong());
		registerType(simpleId++, new DataTypeShort());
		registerType(simpleId++, new DataTypeString());
	}

	public void registerType(int id, DataType<?> dataType) {
		if (dataTypes.containsKey(id))
			throw new IllegalArgumentException("data type with id " + id + " is already registered: "
					+ dataTypes.get(id).getClass().getName());
		dataTypes.put(id, dataType);
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
