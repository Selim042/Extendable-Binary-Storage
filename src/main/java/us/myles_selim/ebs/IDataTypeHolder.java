package us.myles_selim.ebs;

import us.myles_selim.ebs.callbacks.OnWriteCallback;

public interface IDataTypeHolder<T extends IDataTypeHolder<T>> {

	public byte[] serialize();

	/**
	 * Nested IDataTypeHolder objects have callbacks set to flush parent their
	 * parent object. If you are going to replace that callback, please make
	 * sure the parent is still flushed.
	 */
	public T setOnWriteCallback(OnWriteCallback onWrite);

	public void flush();

}
