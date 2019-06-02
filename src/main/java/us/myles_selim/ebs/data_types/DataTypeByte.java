package us.myles_selim.ebs.data_types;

import us.myles_selim.ebs.DataType;
import us.myles_selim.ebs.Storage;

public class DataTypeByte extends DataType<Byte> {

	private byte value;

	public DataTypeByte() {}

	public DataTypeByte(byte value) {
		this.value = value;
	}

	@Override
	public Byte getValue() {
		return this.value;
	}

	@Override
	protected void setValueInternal(Byte value) {
		this.value = value;
	}

	@Override
	protected void setValueObject(Object value) {
		if (value instanceof String) {
			try {
				this.value = Byte.valueOf((String) value);
			} catch (NumberFormatException e) {}
		} else if (this.acceptsValue(value))
			this.value = (byte) value;
	}

	@Override
	public boolean acceptsValue(Object value) {
		if (value instanceof String)
			try {
				Byte.valueOf((String) value);
			} catch (NumberFormatException e) {
				return false;
			}
		return super.acceptsValue(value);
	}

	@Override
	public Class<?>[] accepts() {
		return new Class[] { Byte.class, byte.class };
	}

	@Override
	public void toBytes(Storage ebs) {
		ebs.writeByte(this.value);
	}

	@Override
	public void fromBytes(Storage ebs) {
		this.value = ebs.readByte();
	}

}
