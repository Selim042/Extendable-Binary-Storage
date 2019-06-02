package us.myles_selim.ebs.data_types;

import us.myles_selim.ebs.DataType;
import us.myles_selim.ebs.Storage;

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
	protected void setValueInternal(String value) {
		this.value = value;
	}

	@Override
	protected void setValueObject(Object value) {
		if (value instanceof String)
			this.setValueInternal((String) value);
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

}
