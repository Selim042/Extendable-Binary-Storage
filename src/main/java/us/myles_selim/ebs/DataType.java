package us.myles_selim.ebs;

public abstract class DataType<V> {

	private IDataTypeHolder<?> parent;

	public final void setParent(IDataTypeHolder<?> parent) {
		this.parent = parent;
	}

	public final IDataTypeHolder<?> getParent() {
		return parent;
	}

	public abstract V getValue();

	public final void setValue(V value) {
		setValueInternal(value);
		if (parent != null && parent != getValue())
			parent.flush();
	}

	protected abstract void setValueInternal(V value);

	protected abstract void setValueObject(Object value);

	public abstract Class<?>[] accepts();

	public boolean acceptsValue(Object value) {
		for (Class<?> c : accepts())
			if (c != null && c.isInstance(value))
				return true;
		return false;
	}

	public abstract void toBytes(Storage stor);

	public abstract void fromBytes(Storage stor);

}
