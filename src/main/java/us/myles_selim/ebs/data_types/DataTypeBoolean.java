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
		if (value instanceof String)
			switch (((String) value).toLowerCase()) {
			case "true":
				this.value = true;
			case "false":
				this.value = false;
			default:
				break;
			}
		else if (this.acceptsValue(value))
			this.value = (boolean) value;
	}

	@Override
	public boolean acceptsValue(Object value) {
		if (value instanceof String)
			switch (((String) value).toLowerCase()) {
			case "true":
			case "false":
				return true;
			default:
				break;
			}
		return super.acceptsValue(value);
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
