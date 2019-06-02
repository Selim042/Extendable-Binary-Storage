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
	protected void setValueInternal(Long value) {
		this.value = value;
	}

	@Override
	protected void setValueObject(Object value) {
		if (value instanceof String) {
			try {
				this.value = Long.valueOf((String) value);
			} catch (NumberFormatException e) {}
		} else if (this.acceptsValue(value))
			this.value = (long) value;
	}

	@Override
	public boolean acceptsValue(Object value) {
		if (value instanceof String)
			try {
				Long.valueOf((String) value);
			} catch (NumberFormatException e) {
				return false;
			}
		return super.acceptsValue(value);
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
