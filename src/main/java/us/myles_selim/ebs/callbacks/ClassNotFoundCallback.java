package us.myles_selim.ebs.callbacks;

public abstract class ClassNotFoundCallback {

	public String getNewPath(String oldPath) {
		return oldPath;
	}

	public boolean shouldJustDelete(String oldPath) {
		return false;
	}

}
