package us.myles_selim.ebs;

import java.nio.ByteBuffer;

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
		if (this.acceptsThis(value))
			this.value = (int) value;
	}

	@Override
	public Class<?>[] accepts() {
		return new Class[] { Integer.class, int.class };
	}

	@Override
	public void toBytes(ByteBuffer buf) {
		buf.putInt(this.value);
	}

	@Override
	public void fromBytes(ByteBuffer buf) {
		this.value = buf.getInt();
	}

	public static void main(String... args) {
		DataTypeInteger int1 = new DataTypeInteger(5);
		System.out.println("int1 value: " + int1.getValue());
		ByteBuffer buffer = ByteBuffer.allocate(1000);
		int1.toBytes(buffer);
		buffer.position(0);
		DataTypeInteger int2 = new DataTypeInteger(2);
		System.out.println("int2 init value: " + int2.getValue());
		int2.fromBytes(buffer);
		System.out.println("int2 read value: " + int2.getValue());
	}

}
