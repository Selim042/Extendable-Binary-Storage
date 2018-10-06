package us.myles_selim.ebs.data_types;

import us.myles_selim.ebs.DataType;
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

}
