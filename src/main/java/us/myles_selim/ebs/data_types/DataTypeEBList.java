package us.myles_selim.ebs.data_types;

import us.myles_selim.ebs.DataType;
import us.myles_selim.ebs.EBList;
import us.myles_selim.ebs.Storage;

public class DataTypeEBList extends DataType<EBList<?>> {

	private EBList<?> value;

	@Override
	public EBList<?> getValue() {
		return this.value;
	}

	@Override
	public void setValue(EBList<?> value) {
		this.value = value;
	}

	@Override
	protected void setValueObject(Object value) {
		if (this.acceptsValue(value))
			this.value = (EBList<?>) value;
	}

	@Override
	public Class<?>[] accepts() {
		return new Class[] { EBList.class };
	}

	@Override
	public void toBytes(Storage stor) {
		stor.writeByteArray(this.value.serialize());
	}

	@Override
	public void fromBytes(Storage stor) {
		this.value = EBList.deserialize(stor.readByteArray());
	}

}
