package us.myles_selim.ebs.data_types;

import us.myles_selim.ebs.DataType;
import us.myles_selim.ebs.Storage;

public class DataTypeIntArray extends DataType<int[]> {

	private int[] value;

	public DataTypeIntArray() {}

	public DataTypeIntArray(int[] value) {
		this.value = value;
	}

	@Override
	public int[] getValue() {
		return this.value;
	}

	@Override
	protected void setValueInternal(int[] value) {
		this.value = value;
	}

	@Override
	protected void setValueObject(Object value) {
		if (this.acceptsValue(value))
			this.value = (int[]) value;
	}

	@Override
	public Class<?>[] accepts() {
		return new Class[] { int[].class };
	}

	@Override
	public void toBytes(Storage ebs) {
		ebs.writeIntArray(this.value);
	}

	@Override
	public void fromBytes(Storage ebs) {
		this.value = ebs.readIntArray();
	}

}
