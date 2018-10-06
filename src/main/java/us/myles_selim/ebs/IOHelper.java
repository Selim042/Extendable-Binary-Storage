package us.myles_selim.ebs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class IOHelper {

	public static final String EBS_EXTENSION = ".ebs";

	/**
	 * Writes the given EBStorage to the given File
	 * 
	 * @param storage
	 * @param file
	 * @return true if successfully written, false if otherwise
	 */
	public static boolean writeEBStorage(EBStorage storage, File file) {
		byte[] serializedWrite = storage.serialize();
		try {
			FileOutputStream out = new FileOutputStream(file);
			out.write(serializedWrite);
			out.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Reads a EBStorage from a given File
	 * 
	 * @param file
	 * @return the contents of the file if successfully read, null if otherwise
	 */
	public static EBStorage readEBStorage(File file) {
		byte[] serializedRead = null;
		try {
			FileInputStream in = new FileInputStream(file);
			serializedRead = new byte[in.available()];
			in.read(serializedRead);
			in.close();
		} catch (IOException e) {}
		return EBStorage.deserialize(serializedRead);
	}

}
