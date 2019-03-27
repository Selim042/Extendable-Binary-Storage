package us.myles_selim.ebs;

import us.myles_selim.ebs.data_types.DataTypeString;

@TestClass
public class V1Test {

	public static void main(String... args) {
		System.out.println("--EBStorage--");
		EBStorage stor = new EBStorage().registerPrimitives();
		stor.set("test1", "bleh");
		stor.set("test2", "blerp");
		stor.set("int", 524);
		stor.set("inw", 123);

		EBStorage stor1 = EBStorage.deserialize(stor.serialize());
		System.out.println("stor1 is null: " + (stor1 == null));
		if (stor1 == null)
			return;
		System.out.println(stor1.get("test1"));
		System.out.println(stor1.get("test2"));
		System.out.println(stor1.get("int"));
		System.out.println(stor1.get("inw"));

		System.out.println("\n--EBList--");
		EBList<String> list = new EBList<>(new DataTypeString());
		list.addWrapped("Hello ");
		list.addWrapped("World!");

		EBList<String> list2 = EBList.deserialize(list.serialize());
		for (DataType<String> i : list2)
			System.out.println(i.getValue());
	}

}
