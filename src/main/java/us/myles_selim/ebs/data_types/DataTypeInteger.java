package us.myles_selim.ebs.data_types;

import us.myles_selim.ebs.DataType;
import us.myles_selim.ebs.Storage;

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
		if (value instanceof String) {
			try {
				this.value = Integer.valueOf((String) value);
			} catch (NumberFormatException e) {}
		} else if (this.acceptsValue(value))
			this.value = (int) value;
	}

	@Override
	public boolean acceptsValue(Object value) {
		if (value instanceof String)
			try {
				Integer.valueOf((String) value);
			} catch (NumberFormatException e) {
				return false;
			}
		return super.acceptsValue(value);
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

}
