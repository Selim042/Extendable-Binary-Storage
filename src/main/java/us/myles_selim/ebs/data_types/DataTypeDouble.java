package us.myles_selim.ebs.data_types;

import us.myles_selim.ebs.Storage;

public class DataTypeDouble extends DataType<Double> {

	private double value;

	public DataTypeDouble() {}

	public DataTypeDouble(double value) {
		this.value = value;
	}

	@Override
	public Double getValue() {
		return this.value;
	}

	@Override
	public void setValue(Double value) {
		this.value = value;
	}

	@Override
	protected void setValueObject(Object value) {
		if (this.acceptsValue(value))
			this.value = (double) value;
	}

	@Override
	public Class<?>[] accepts() {
		return new Class[] { Double.class, double.class };
	}

	@Override
	public void toBytes(Storage ebs) {
		ebs.writeDouble(this.value);
	}

	@Override
	public void fromBytes(Storage ebs) {
		this.value = ebs.readDouble();
	}

	public static void main(String... args) {
		DataTypeDouble double1 = new DataTypeDouble(5);
		System.out.println("double1 value: " + double1.getValue());
		Storage storage = new Storage();
		double1.toBytes(storage);
		DataTypeDouble double2 = new DataTypeDouble(2);
		System.out.println("double2 init value: " + double2.getValue());
		double2.fromBytes(storage);
		System.out.println("double2 read value: " + double2.getValue());
	}

}
