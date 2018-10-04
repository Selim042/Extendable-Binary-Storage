package us.myles_selim.ebs;

public class DataTypeString extends DataType<String> {

	private String value;

	public DataTypeString() {}

	public DataTypeString(String value) {
		this.value = value;
	}

	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	protected void setValueObject(Object value) {
		if (value instanceof String)
			this.setValue((String) value);
	}

	@Override
	public Class<?>[] accepts() {
		return new Class[] { String.class };
	}

	@Override
	public void toBytes(Storage ebs) {
		ebs.writeString(this.value);
	}

	@Override
	public void fromBytes(Storage ebs) {
		this.value = ebs.readString();
	}

	public static void main(String... args) {
		DataTypeString string1 = new DataTypeString("Hello, World!");
		System.out.println("string1 value: " + string1.getValue());
		Storage storage = new Storage();
		string1.toBytes(storage);
		DataTypeString string2 = new DataTypeString("Banana!");
		System.out.println("string2 init value: " + string2.getValue());
		string2.fromBytes(storage);
		System.out.println("string2 read value: " + string2.getValue());
	}

}
