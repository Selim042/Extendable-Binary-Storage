package us.myles_selim.ebs;

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

	public static void main(String... args) {
		DataTypeShort short1 = new DataTypeShort((short) 5);
		System.out.println("short1 value: " + short1.getValue());
		Storage storage = new Storage();
		short1.toBytes(storage);
		DataTypeShort short2 = new DataTypeShort((short) -2);
		System.out.println("short2 init value: " + short2.getValue());
		short2.fromBytes(storage);
		System.out.println("short2 read value: " + short2.getValue());
	}

}
