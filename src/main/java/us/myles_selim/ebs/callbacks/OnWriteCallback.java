package us.myles_selim.ebs.callbacks;

import us.myles_selim.ebs.EBList;
import us.myles_selim.ebs.EBStorage;

public abstract class OnWriteCallback {

	@Deprecated
	public void onWrite() {}

	public void onWriteEBS(EBStorage ebs) {
		onWrite();
	}

	public void onWriteEBL(EBList<?> ebs) {
		onWrite();
	}

}
