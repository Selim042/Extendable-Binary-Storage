package us.myles_selim.ebs.data_types;

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
	public void setValue(Byte value) {
		this.value = value;
	}

	@Override
	protected void setValueObject(Object value) {
		if (this.acceptsValue(value))
			this.value = (byte) value;
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

	public static void main(String... args) {
		DataTypeByte byte1 = new DataTypeByte((byte) 5);
		System.out.println("byte1 value: " + byte1.getValue());
		Storage storage = new Storage();
		byte1.toBytes(storage);
		DataTypeByte byte2 = new DataTypeByte((byte) 2);
		System.out.println("byte2 init value: " + byte2.getValue());
		byte2.fromBytes(storage);
		System.out.println("byte2 read value: " + byte2.getValue());
	}

}
