package us.myles_selim.ebs.data_types;

import us.myles_selim.ebs.DataType;
import us.myles_selim.ebs.Storage;

public class DataTypeByteArray extends DataType<byte[]> {

	private byte[] value;

	public DataTypeByteArray() {}

	public DataTypeByteArray(byte[] value) {
		this.value = value;
	}

	@Override
	public byte[] getValue() {
		return this.value;
	}

	@Override
	public void setValue(byte[] value) {
		this.value = value;
	}

	@Override
	protected void setValueObject(Object value) {
		if (this.acceptsValue(value))
			this.value = (byte[]) value;
	}

	@Override
	public Class<?>[] accepts() {
		return new Class[] { byte[].class };
	}

	@Override
	public void toBytes(Storage ebs) {
		ebs.writeByteArray(this.value);
	}

	@Override
	public void fromBytes(Storage ebs) {
		this.value = ebs.readByteArray();
	}

}
