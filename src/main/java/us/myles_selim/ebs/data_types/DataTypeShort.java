package us.myles_selim.ebs.data_types;

import us.myles_selim.ebs.DataType;
import us.myles_selim.ebs.Storage;

public class DataTypeShort extends DataType<Short> {

	private short value;

	public DataTypeShort() {}

	public DataTypeShort(short value) {
		this.value = value;
	}

	@Override
	public Short getValue() {
		return this.value;
	}

	@Override
	protected void setValueInternal(Short value) {
		this.value = value;
	}

	@Override
	protected void setValueObject(Object value) {
		if (value instanceof String) {
			try {
				this.value = Short.valueOf((String) value);
			} catch (NumberFormatException e) {}
		} else if (this.acceptsValue(value))
			this.value = (short) value;
	}

	@Override
	public boolean acceptsValue(Object value) {
		if (value instanceof String)
			try {
				Short.valueOf((String) value);
			} catch (NumberFormatException e) {
				return false;
			}
		return super.acceptsValue(value);
	}

	@Override
	public Class<?>[] accepts() {
		return new Class[] { Short.class, short.class };
	}

	@Override
	public void toBytes(Storage ebs) {
		ebs.writeShort(this.value);
	}

	@Override
	public void fromBytes(Storage ebs) {
		this.value = ebs.readShort();
	}

}
