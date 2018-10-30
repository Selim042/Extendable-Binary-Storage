package us.myles_selim.ebs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

public class IOHelper {

	public static final String EBS_EXTENSION = ".ebs";
	public static final String EBS_LIST_EXTENSION = ".ebl";
	public static final FilenameFilter FILE_FILTER = new FilenameFilter() {

		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(IOHelper.EBS_EXTENSION);
		}
	};

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
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EBStorage.deserialize(serializedRead);
	}

	public static boolean writeEBList(EBList<?> list, File file) {
		byte[] ser = list.serialize();
		try {
			FileOutputStream out = new FileOutputStream(file);
			out.write(ser);
			out.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public static <W> EBList<W> readEBList(File file) {
		byte[] ser = null;
		try {
			FileInputStream in = new FileInputStream(file);
			ser = new byte[in.available()];
			in.read(ser);
			in.close();
		} catch (IOException e) {}
		return EBList.deserialize(ser);
	}

}
