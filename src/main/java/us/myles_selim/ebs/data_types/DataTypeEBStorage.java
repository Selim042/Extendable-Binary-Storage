package us.myles_selim.ebs.data_types;

import us.myles_selim.ebs.DataType;
import us.myles_selim.ebs.EBStorage;
import us.myles_selim.ebs.Storage;
import us.myles_selim.ebs.callbacks.ParentFlushCallback;

public class DataTypeEBStorage extends DataType<EBStorage> {

	private EBStorage value;

	@Override
	public EBStorage getValue() {
		return this.value;
	}

	@Override
	protected void setValueInternal(EBStorage value) {
		this.value = value;
	}

	@Override
	protected void setValueObject(Object value) {
		if (this.acceptsValue(value)) {
			this.value = (EBStorage) value;
			if (this.value != null)
				this.value.setOnWriteCallback(new ParentFlushCallback(this.value));
		}
	}

	@Override
	public Class<?>[] accepts() {
		return new Class[] { EBStorage.class };
	}

	@Override
	public void toBytes(Storage stor) {
		stor.writeByteArray(this.value.serialize());
	}

	@Override
	public void fromBytes(Storage stor) {
		this.value = EBStorage.deserialize(stor.readByteArray());
	}

}
