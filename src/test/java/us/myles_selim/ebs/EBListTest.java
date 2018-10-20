package us.myles_selim.ebs;

import java.io.File;

import us.myles_selim.ebs.data_types.DataTypeString;

public class EBListTest {

	public static void main(String... args) {
		File file = new File("listIOTest" + IOHelper.EBS_EXTENSION);

		EBList<String, DataTypeString> list1 = new EBList<>(new DataTypeString());
		list1.addWrapped("Hello, ");
		list1.addWrapped("World!");
		IOHelper.writeEBList(list1, file);

		EBList<String, DataTypeString> list2 = IOHelper.readEBList(file);
		for (String s : list2.values())
			System.out.print(s);
		System.out.println();
	}

}
