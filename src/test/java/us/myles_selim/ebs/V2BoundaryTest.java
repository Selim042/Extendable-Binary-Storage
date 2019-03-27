package us.myles_selim.ebs;

@TestClass
public class V2BoundaryTest {

	public static void main(String... args) {
		// Storage stor = new Storage();
		// new DataTypeTest().toBytes(stor);
		// new DataTypeTest().fromBytes(stor);

		EBStorage ebs1 = new EBStorage().registerType(new DataTypeTest());
		ebs1.set("t", "s");
		ebs1.set("2", "b");
		byte[] ser = ebs1.serialize();

		EBStorage ebs2 = EBStorage.deserialize(ser);

	}

	private static class DataTypeTest extends DataType<Integer> {

		public DataTypeTest() {}

		@Override
		public Integer getValue() {
			return 1;
		}

		@Override
		public void setValue(Integer value) {}

		@Override
		protected void setValueObject(Object value) {}

		@Override
		public Class<?>[] accepts() {
			return new Class[] { String.class };
		}

		@Override
		public void toBytes(Storage stor) {
			stor.writeInt(1);
			stor.writeBoolean(true);
			stor.writeLong(1L);
			stor.writeInt(2);
			// stor.writeInt(3);
			// stor.writeBoolean(false);
			// stor.writeLong(234523452345L);
			// stor.writeInt(4);
		}

		@Override
		public void fromBytes(Storage stor) {
			System.out.println(stor.readInt());
			System.out.println(stor.readBoolean());
			System.out.println(stor.readLong());
			System.out.println(stor.readInt());
			System.out.println(stor.readInt());
			System.out.println(stor.readBoolean());
			System.out.println(stor.readLong());
			System.out.println(stor.readInt());
		}

	}

}
