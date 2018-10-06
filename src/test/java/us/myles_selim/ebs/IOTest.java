package us.myles_selim.ebs;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class IOTest {

	public static void main(String... args) {
		EBStorage storage = new EBStorage().registerPrimitives();
		storage.set("testInt", 5);
		storage.set("string", "bleh");
		storage.set("testDouble", 123.455);

		byte[] serializedWrite = storage.serialize();
		try {
			FileOutputStream out = new FileOutputStream("ioTest.ebs");
			out.write(serializedWrite);
			out.close();
		} catch (IOException e) {}

		byte[] serializedRead = null;
		try {
			FileInputStream in = new FileInputStream("ioTest.ebs");
			serializedRead = new byte[in.available()];
			in.read(serializedRead);
			in.close();
		} catch (IOException e) {}

		EBStorage storage2 = EBStorage.deserialize(serializedRead);
		System.out.println(storage2.get("testInt"));
		System.out.println(storage2.get("string"));
		System.out.println(storage2.get("testDouble"));
	}

}
