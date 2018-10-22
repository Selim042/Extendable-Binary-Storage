package us.myles_selim.ebs.data_types;

import us.myles_selim.ebs.DataType;
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
		if (value instanceof String) {
			try {
				this.value = Double.valueOf((String) value);
			} catch (NumberFormatException e) {}
		} else if (this.acceptsValue(value))
			this.value = (double) value;
	}

	@Override
	public boolean acceptsValue(Object value) {
		if (value instanceof String)
			try {
				Double.valueOf((String) value);
			} catch (NumberFormatException e) {
				return false;
			}
		return super.acceptsValue(value);
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

}
