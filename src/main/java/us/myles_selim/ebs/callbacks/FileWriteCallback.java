package us.myles_selim.ebs.callbacks;

import java.io.File;

import us.myles_selim.ebs.EBList;
import us.myles_selim.ebs.EBStorage;
import us.myles_selim.ebs.IOHelper;

public class FileWriteCallback extends OnWriteCallback {

	private final File file;

	public FileWriteCallback(File file) {
		this.file = file;
	}

	@Override
	public void onWriteEBS(EBStorage ebs) {
		IOHelper.writeEBStorage(ebs, this.file);
	}

	@Override
	public void onWriteEBL(EBList<?> ebl) {
		IOHelper.writeEBList(ebl, this.file);
	}

}
