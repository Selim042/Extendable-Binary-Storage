package us.myles_selim.ebs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExtendableBinaryStorage {

	private static final Map<Integer, Class<? extends DataType<?>>> DATA_TYPES = new ConcurrentHashMap<>();

	public static void registerType(int id, Class<? extends DataType<?>> dataType) {
		if (DATA_TYPES.containsKey(id))
			throw new IllegalArgumentException("data type with id " + id + " is already registered: "
					+ DATA_TYPES.get(id).getName());
		DATA_TYPES.put(id, dataType);
	}

}
