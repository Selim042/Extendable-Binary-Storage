package us.myles_selim.ebs;

import java.io.File;

import us.myles_selim.ebs.callbacks.ClassNotFoundCallback;

@TestClass
public class V3RepackageTest {

	public static void main(String... args) {
		// EBStorage ebs1 = new EBStorage().registerType(new
		// V3Test.V3TestDataType());
		// byte[] ser = ebs1.serialize();
		//
		// IOHelper.writeEBStorage(ebs1, new File("v3RepackageTest.ebs"));
		File file = new File("v3RepackageTest.ebs");
		IOHelper.writeEBStorage(IOHelper.readEBStorage(file, new ClassNotFoundCallback() {

			@Override
			public String getNewPath(String oldPath) {
				System.out.println("oldPath: " + oldPath);
				// return oldPath;
				switch (oldPath) {
				case "us.myles_selim.ebs.V3Test$V3TestDataType":
					return "us.myles_selim.ebs.v3_test.V3Test$V3TestDataType";
				default:
					return oldPath;
				}
			}
		}), file);
	}

}
