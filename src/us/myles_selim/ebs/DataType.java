package us.myles_selim.ebs;

public abstract class DataType<V> {

	public abstract V getValue();

	public abstract void setValue(V value);

	protected abstract void setValueObject(Object value);

	public abstract Class<?>[] accepts();

	public boolean acceptsValue(Object value) {
		for (Class<?> c : accepts())
			if (c != null && c.isInstance(value))
				return true;
		return false;
	}

	public abstract void toBytes(Storage buf);

	public abstract void fromBytes(Storage but);

}
