package us.myles_selim.ebs;

public class DataTypeInteger extends DataType<Integer> {

	private int value;

	public DataTypeInteger() {}

	public DataTypeInteger(int value) {
		this.value = value;
	}

	@Override
	public Integer getValue() {
		return this.value;
	}

	@Override
	public void setValue(Integer value) {
		this.value = value;
	}

	@Override
	protected void setValueObject(Object value) {
		if (this.acceptsValue(value))
			this.value = (int) value;
	}

	@Override
	public Class<?>[] accepts() {
		return new Class[] { Integer.class, int.class };
	}

	@Override
	public void toBytes(Storage ebs) {
		ebs.writeInt(this.value);
	}

	@Override
	public void fromBytes(Storage ebs) {
		this.value = ebs.readInt();
	}

	public static void main(String... args) {
		DataTypeInteger int1 = new DataTypeInteger(5);
		System.out.println("int1 value: " + int1.getValue());
		Storage storage = new Storage();
		int1.toBytes(storage);
		DataTypeInteger int2 = new DataTypeInteger(2);
		System.out.println("int2 init value: " + int2.getValue());
		int2.fromBytes(storage);
		System.out.println("int2 read value: " + int2.getValue());
	}

}
