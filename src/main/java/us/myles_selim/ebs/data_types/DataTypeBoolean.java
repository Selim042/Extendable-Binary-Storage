package us.myles_selim.ebs.data_types;

import us.myles_selim.ebs.Storage;

public class DataTypeBoolean extends DataType<Boolean> {

	private boolean value;

	public DataTypeBoolean() {}

	public DataTypeBoolean(boolean value) {
		this.value = value;
	}

	@Override
	public Boolean getValue() {
		return this.value;
	}

	@Override
	public void setValue(Boolean value) {
		this.value = value;
	}

	@Override
	protected void setValueObject(Object value) {
		if (this.acceptsValue(value))
			this.value = (boolean) value;
	}

	@Override
	public Class<?>[] accepts() {
		return new Class[] { Boolean.class, boolean.class };
	}

	@Override
	public void toBytes(Storage ebs) {
		ebs.writeBoolean(this.value);
	}

	@Override
	public void fromBytes(Storage ebs) {
		this.value = ebs.readBoolean();
	}

	public static void main(String... args) {
		DataTypeBoolean bool1 = new DataTypeBoolean(true);
		System.out.println("bool1 value: " + bool1.getValue());
		Storage storage = new Storage();
		bool1.toBytes(storage);
		DataTypeBoolean bool2 = new DataTypeBoolean(false);
		System.out.println("bool2 init value: " + bool2.getValue());
		bool2.fromBytes(storage);
		System.out.println("bool2 read value: " + bool2.getValue());
	}

}
