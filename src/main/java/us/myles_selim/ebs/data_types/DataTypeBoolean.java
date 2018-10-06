package us.myles_selim.ebs.data_types;

import us.myles_selim.ebs.DataType;
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

}
