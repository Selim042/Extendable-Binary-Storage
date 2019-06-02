package us.myles_selim.ebs.data_types;

import us.myles_selim.ebs.DataType;
import us.myles_selim.ebs.EBList;
import us.myles_selim.ebs.Storage;
import us.myles_selim.ebs.callbacks.ParentFlushCallback;

public class DataTypeEBList extends DataType<EBList<?>> {

	private EBList<?> value;

	@Override
	public EBList<?> getValue() {
		return this.value;
	}

	@Override
	protected void setValueInternal(EBList<?> value) {
		this.value = value;
	}

	@Override
	protected void setValueObject(Object value) {
		if (this.acceptsValue(value)) {
			this.value = (EBList<?>) value;
			if (this.value != null)
				this.value.setOnWriteCallback(new ParentFlushCallback(this.value));
		}
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
