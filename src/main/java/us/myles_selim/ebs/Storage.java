package us.myles_selim.ebs;

import java.util.ArrayList;
import java.util.List;

public class Storage {

	public static final int CURRENT_VERSION = 1;

	private static final String HEADER = "[EBS]";

	private final List<Byte> data;
	private int version;
	private int writePos = 0;
	int readPos = 0;

	private int maxReadMarker = -1;
	private int markerPos = -1;

	protected Storage() {
		this.data = new ArrayList<>();
		this.version = CURRENT_VERSION;
		writeInt(this.version);
		writeString(HEADER);
		this.readPos = 8 + (HEADER.length() * 2);
	}

	protected Storage(byte[] data) {
		this.data = new ArrayList<>();
		if (data != null)
			for (byte b : data)
				this.data.add(b);
		this.version = readInt();
		int headerLength = readInt();
		if (headerLength != HEADER.length())
			this.version = 0;
		switch (this.version) {
		case 0:
			this.readPos = 0;
			break;
		case 1:
			this.readPos = 8 + (HEADER.length() * 2);
			break;
		default:
			throw new IllegalArgumentException(
					"version " + this.version + " is not supported in this build");
		}
	}

	public int getVersion() {
		return this.version;
	}

	public void writeBoolean(boolean val) {
		if (val)
			data.add(writePos++, (byte) 1);
		else
			data.add(writePos++, (byte) 0);
	}

	public boolean readBoolean() {
		if (readByte() == 0)
			return false;
		return true;
	}

	public void writeByte(byte val) {
		data.add(writePos++, val);
	}

	public byte readByte() {
		if (readPos + 1 > data.size() || (maxReadMarker != -1 && readPos + 1 > maxReadMarker))
			return 0;
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
		if (readPos + 2 > data.size() || (maxReadMarker != -1 && readPos + 2 > maxReadMarker))
			return 0;
		return (short) ((0xff & data.get(readPos++)) << 8 | (0xff & data.get(readPos++)));
	}

	public void writeInt(int val) {
		data.add(writePos++, (byte) ((val >> 24) & 0xff));
		data.add(writePos++, (byte) ((val >> 16) & 0xff));
		data.add(writePos++, (byte) ((val >> 8) & 0xff));
		data.add(writePos++, (byte) ((val) & 0xff));
	}

	public int readInt() {
		if (readPos + 4 > data.size() || (maxReadMarker != -1 && readPos + 4 > maxReadMarker))
			return 0;
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
		if (readPos + 8 > data.size() || (maxReadMarker != -1 && readPos + 8 > maxReadMarker))
			return 0;
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
		if (length == 0)
			return null;
		StringBuilder v = new StringBuilder();
		for (int i = 0; i < length; i++)
			v.append(readChar());
		return v.toString();
	}

	public void writeByteArray(byte[] val) {
		writeInt(val.length);
		for (byte b : val)
			writeByte(b);
	}

	public byte[] readByteArray() {
		int length = readInt();
		byte[] ret = new byte[length];
		for (int i = 0; i < length; i++)
			ret[i] = readByte();
		return ret;
	}

	public void writeIntArray(int[] val) {
		writeInt(val.length);
		for (int i : val)
			writeInt(i);
	}

	public int[] readIntArray() {
		int length = readInt();
		int[] ret = new int[length];
		for (int i = 0; i < length; i++)
			ret[i] = readInt();
		return ret;
	}

	public byte[] getAsByteArray() {
		byte[] vals = new byte[data.size()];
		for (int i = 0; i < vals.length; i++)
			vals[i] = data.get(i);
		return vals;
	}

	protected void setReadDistance(int dist) {
		this.maxReadMarker = this.readPos + dist - 4;
	}

	protected void clearReadDistance() {
		this.readPos = this.maxReadMarker;
		this.maxReadMarker = -1;
	}

	protected void setLengthMarker() {
		this.markerPos = this.writePos;
		this.writeInt(10);
	}

	protected boolean writeLengthMarker() {
		if (this.markerPos == -1)
			return false;
		int oldPos = this.writePos;
		this.writePos = this.markerPos;

		int length = oldPos - this.markerPos;
		data.set(writePos++, (byte) ((length >> 24) & 0xff));
		data.set(writePos++, (byte) ((length >> 16) & 0xff));
		data.set(writePos++, (byte) ((length >> 8) & 0xff));
		data.set(writePos++, (byte) ((length) & 0xff));

		this.writePos = oldPos;
		this.markerPos = -1;
		return true;
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
		byte[] data = d.getAsByteArray();
		for (int i = 0; i < data.length; i++)
			System.out.print(data[i] + (i == data.length - 1 ? "" : ","));
		System.out.println("]");
		System.out.println(getHex(data));
		System.out.println("Version: " + d.getVersion());
	}

	private static final String HEXES = "0123456789ABCDEF";

	private static String getHex(byte[] raw) {
		final StringBuilder hex = new StringBuilder(2 * raw.length);
		for (final byte b : raw)
			hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
		return hex.toString();
	}

}
