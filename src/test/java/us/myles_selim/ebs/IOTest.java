package us.myles_selim.ebs;

import java.io.File;

public class IOTest {

	public static void main(String... args) {
		EBStorage storage = new EBStorage().registerPrimitives();
		storage.set("testInt", 5);
		storage.set("string", "bleh");
		storage.set("testDouble", 123.455);
		IOHelper.writeEBStorage(storage, new File("ioHelperTest.ebs"));

		EBStorage storage2 = IOHelper.readEBStorage(new File("ioHelperTest.ebs"));
		System.out.println(storage2.get("testInt"));
		System.out.println(storage2.get("string"));
		System.out.println(storage2.get("testDouble"));
	}

}
