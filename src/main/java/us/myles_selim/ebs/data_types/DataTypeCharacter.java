package us.myles_selim.ebs.data_types;

import us.myles_selim.ebs.Storage;

public class DataTypeCharacter extends DataType<Character> {

	private char value;

	public DataTypeCharacter() {}

	public DataTypeCharacter(char value) {
		this.value = value;
	}

	@Override
	public Character getValue() {
		return this.value;
	}

	@Override
	public void setValue(Character value) {
		this.value = value;
	}

	@Override
	protected void setValueObject(Object value) {
		if (this.acceptsValue(value))
			this.value = (char) value;
	}

	@Override
	public Class<?>[] accepts() {
		return new Class[] { Character.class, char.class };
	}

	@Override
	public void toBytes(Storage ebs) {
		ebs.writeChar(this.value);
	}

	@Override
	public void fromBytes(Storage ebs) {
		this.value = ebs.readChar();
	}

	public static void main(String... args) {
		DataTypeCharacter char1 = new DataTypeCharacter('e');
		System.out.println("char1 value: " + char1.getValue());
		Storage storage = new Storage();
		char1.toBytes(storage);
		DataTypeCharacter char2 = new DataTypeCharacter('i');
		System.out.println("char2 init value: " + char2.getValue());
		char2.fromBytes(storage);
		System.out.println("char2 read value: " + char2.getValue());
	}

}
