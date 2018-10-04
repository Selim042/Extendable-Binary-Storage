package us.myles_selim.ebs;

public class DataTypeFloat extends DataType<Float> {

	private float value;

	public DataTypeFloat() {}

	public DataTypeFloat(int value) {
		this.value = value;
	}

	@Override
	public Float getValue() {
		return this.value;
	}

	@Override
	public void setValue(Float value) {
		this.value = value;
	}

	@Override
	protected void setValueObject(Object value) {
		if (this.acceptsValue(value))
			this.value = (float) value;
	}

	@Override
	public Class<?>[] accepts() {
		return new Class[] { Float.class, float.class };
	}

	@Override
	public void toBytes(Storage ebs) {
		ebs.writeFloat(this.value);
	}

	@Override
	public void fromBytes(Storage ebs) {
		this.value = ebs.readFloat();
	}

	public static void main(String... args) {
		DataTypeFloat float1 = new DataTypeFloat(5);
		System.out.println("float1 value: " + float1.getValue());
		Storage storage = new Storage();
		float1.toBytes(storage);
		DataTypeFloat float2 = new DataTypeFloat(2);
		System.out.println("float2 init value: " + float2.getValue());
		float2.fromBytes(storage);
		System.out.println("float2 read value: " + float2.getValue());
	}

}
