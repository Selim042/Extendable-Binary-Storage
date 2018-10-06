package us.myles_selim.ebs;

import java.util.ArrayList;
import java.util.List;

public class Storage {

	private final List<Byte> data;
	private int writePos = 0;
	private int readPos = 0;

	protected Storage() {
		this.data = new ArrayList<>();
	}

	protected Storage(byte[] data) {
		this();
		if (data != null)
			for (byte b : data)
				this.data.add(b);
	}

	public void writeBoolean(boolean val) {
		if (val)
			data.add(writePos++, (byte) 1);
		else
			data.add(writePos++, (byte) 0);
	}

	public boolean readBoolean() {
		byte b = data.get(readPos++);
		if (b == 0)
			return false;
		return true;
	}

	public void writeByte(byte val) {
		data.add(writePos++, val);
	}

	public byte readByte() {
		return data.get(readPos++);
	}

	public void writeChar(char val) {
		writeShort((short) val);
	}

	public char readChar() {
		return (char) readShort();
	}

	public void writeShort(short val) {
		data.add(writePos++, (byte) ((val >> 8) & 0xff));
		data.add(writePos++, (byte) ((val) & 0xff));
	}

	public short readShort() {
		return (short) ((0xff & data.get(readPos++)) << 8 | (0xff & data.get(readPos++)));
	}

	public void writeInt(int val) {
		data.add(writePos++, (byte) ((val >> 24) & 0xff));
		data.add(writePos++, (byte) ((val >> 16) & 0xff));
		data.add(writePos++, (byte) ((val >> 8) & 0xff));
		data.add(writePos++, (byte) ((val) & 0xff));
	}

	public int readInt() {
		return ((0xff & data.get(readPos++)) << 24 | (0xff & data.get(readPos++)) << 16
				| (0xff & data.get(readPos++)) << 8 | (0xff & data.get(readPos++)));
	}

	public void writeLong(long val) {
		data.add(writePos++, (byte) ((val >> 56) & 0xFF));
		data.add(writePos++, (byte) ((val >> 48) & 0xFF));
		data.add(writePos++, (byte) ((val >> 40) & 0xFF));
		data.add(writePos++, (byte) ((val >> 32) & 0xFF));
		data.add(writePos++, (byte) ((val >> 24) & 0xFF));
		data.add(writePos++, (byte) ((val >> 16) & 0xFF));
		data.add(writePos++, (byte) ((val >> 8) & 0xFF));
		data.add(writePos++, (byte) ((val) & 0xFF));
	}

	public long readLong() {
		return ((long) (0xFF & data.get(readPos++)) << 56 | (long) (0xFF & data.get(readPos++)) << 48
				| (long) (0xFF & data.get(readPos++)) << 40 | (long) (0xFF & data.get(readPos++)) << 32
				| (long) (0xFF & data.get(readPos++)) << 24 | (long) (0xFF & data.get(readPos++)) << 16
				| (long) (0xFF & data.get(readPos++)) << 8 | (long) (0xFF & data.get(readPos++)));
	}

	public void writeFloat(float val) {
		writeInt(Float.floatToIntBits(val));
	}

	public float readFloat() {
		return Float.intBitsToFloat(readInt());
	}

	public void writeDouble(double val) {
		writeLong(Double.doubleToLongBits(val));
	}

	public double readDouble() {
		return Double.longBitsToDouble(readLong());
	}

	public void writeString(String val) {
		if (val == null) {
			writeInt(0);
			return;
		}
		char[] arr = val.toCharArray();
		writeInt(arr.length);
		for (char c : arr)
			writeChar(c);
	}

	public String readString() {
		int length = readInt();
		String v = "";
		for (int i = 0; i < length; i++)
			v += readChar();
		return v;
	}

	public byte[] getByteArray() {
		byte[] vals = new byte[data.size()];
		for (int i = 0; i < vals.length; i++)
			vals[i] = data.get(i);
		return vals;
	}

	public static void main(String... args) {
		Storage d = new Storage();
		d.writeInt(2);
		System.out.println(d.readInt());
		d.writeShort((short) (Short.MAX_VALUE - 2));
		System.out.println(d.readShort());
		d.writeBoolean(false);
		System.out.println(d.readBoolean());
		d.writeByte((byte) -4);
		System.out.println(d.readByte());
		d.writeChar('H');
		System.out.println(d.readChar());
		d.writeLong(-1234567890123456789l);
		System.out.println(d.readLong());
		d.writeDouble(-213.41232);
		System.out.println(d.readDouble());
		d.writeFloat(3.141592f);
		System.out.println(d.readFloat());
		d.writeString("Hello World!");
		System.out.println(d.readString());
		System.out.print("[");
		byte[] data = d.getByteArray();
		for (int i = 0; i < data.length; i++)
			System.out.print(data[i] + (i == data.length - 1 ? "" : ","));
		System.out.println("]");
		System.out.println(getHex(data));
	}

	private static final String HEXES = "0123456789ABCDEF";

	private static String getHex(byte[] raw) {
		final StringBuilder hex = new StringBuilder(2 * raw.length);
		for (final byte b : raw)
			hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
		return hex.toString();
	}

}
