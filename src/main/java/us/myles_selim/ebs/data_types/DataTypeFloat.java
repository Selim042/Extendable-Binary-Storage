package us.myles_selim.ebs.data_types;

import us.myles_selim.ebs.DataType;
import us.myles_selim.ebs.Storage;

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

}
