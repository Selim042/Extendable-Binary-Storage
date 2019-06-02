package us.myles_selim.ebs.callbacks;

import us.myles_selim.ebs.EBList;
import us.myles_selim.ebs.EBStorage;
import us.myles_selim.ebs.IDataTypeHolder;

public class ParentFlushCallback extends OnWriteCallback {

	private IDataTypeHolder<?> parent;

	public ParentFlushCallback(IDataTypeHolder<?> parent) {
		this.parent = parent;
	}

	@Override
	public void onWriteEBS(EBStorage ebs) {
		if (parent != null && ebs != parent)
			parent.flush();
	}

	@Override
	public void onWriteEBL(EBList<?> ebs) {
		if (parent != null)
			parent.flush();
	}

}