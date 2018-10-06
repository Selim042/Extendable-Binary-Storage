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
	public void setValue(Short value) {
		this.value = value;
	}

	@Override
	protected void setValueObject(Object value) {
		if (this.acceptsValue(value))
			this.value = (short) value;
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
