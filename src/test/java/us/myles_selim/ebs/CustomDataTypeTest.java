package us.myles_selim.ebs;

class CustomDataTypeTest {

	private static class CustomData {

		public int test = 2;

		public CustomData(int val) {
			this.test = val;
		}

	}

	private static class DataTypeCustomData extends DataType<CustomData> {

		private CustomData data;

		public DataTypeCustomData() {}

		@Override
		public CustomData getValue() {
			return this.data;
		}

		@Override
		public void setValue(CustomData value) {
			this.data = value;
		}

		@Override
		protected void setValueObject(Object value) {
			if (value instanceof CustomData)
				this.data = (CustomData) value;
		}

		@Override
		public Class<?>[] accepts() {
			return new Class[] { CustomData.class };
		}

		@Override
		public void toBytes(Storage ebs) {
			ebs.writeInt(data.test);
		}

		@Override
		public void fromBytes(Storage ebs) {
			if (data == null)
				data = new CustomData(ebs.readInt());
			else
				data.test = ebs.readInt();
		}

	}

	public static void main(String... args) {
		EBStorage storage = new EBStorage().registerType(new DataTypeCustomData());
		storage.set("customData", new CustomData(42));
		CustomData dat = storage.get("customData", CustomData.class);
		System.out.println(dat.test);
	}

}
