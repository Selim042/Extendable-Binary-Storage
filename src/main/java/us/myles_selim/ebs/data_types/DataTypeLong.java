package us.myles_selim.ebs.data_types;

import us.myles_selim.ebs.DataType;
import us.myles_selim.ebs.Storage;

public class DataTypeLong extends DataType<Long> {

	private long value;

	public DataTypeLong() {}

	public DataTypeLong(long value) {
		this.value = value;
	}

	@Override
	public Long getValue() {
		return this.value;
	}

	@Override
	public void setValue(Long value) {
		this.value = value;
	}

	@Override
	protected void setValueObject(Object value) {
		if (this.acceptsValue(value))
			this.value = (long) value;
	}

	@Override
	public Class<?>[] accepts() {
		return new Class[] { Long.class, long.class };
	}

	@Override
	public void toBytes(Storage ebs) {
		ebs.writeLong(this.value);
	}

	@Override
	public void fromBytes(Storage ebs) {
		this.value = ebs.readLong();
	}

}
