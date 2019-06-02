package us.myles_selim.ebs;

import java.io.File;

import us.myles_selim.ebs.callbacks.FileWriteCallback;

@TestClass
public class NestedEBSFlushTest {

	public static void main(String... args) {
		File file = new File("nestedEbsTest.ebs");
		EBStorage out1 = new EBStorage().registerPrimitives();
		out1.setOnWriteCallback(new FileWriteCallback(file));
		EBStorage in1 = new EBStorage().registerPrimitives();
		out1.set("nested", in1);
		in1.set("testKey", true);

		System.out.println(IOHelper.readEBStorage(file).get("nested", EBStorage.class).get("testKey"));
		IOHelper.readEBStorage(new File("nestedEbsTest.ebs"))
				.setOnWriteCallback(new FileWriteCallback(file)).get("nested", EBStorage.class)
				.set("testKey", 42);
		System.out.println(IOHelper.readEBStorage(file).get("nested", EBStorage.class).get("testKey"));
	}

}
