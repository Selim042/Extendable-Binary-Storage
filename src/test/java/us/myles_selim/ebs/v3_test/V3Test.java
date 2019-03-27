package us.myles_selim.ebs.v3_test;

import us.myles_selim.ebs.DataType;
import us.myles_selim.ebs.Storage;

public class V3Test {

	private static class V3TestData {

		public int test = 2;

		public V3TestData(int val) {
			this.test = val;
		}

	}

	public static class V3TestDataType extends DataType<V3TestData> {

		private V3TestData data;

		public V3TestDataType() {}

		@Override
		public V3TestData getValue() {
			return this.data;
		}

		@Override
		public void setValue(V3TestData value) {
			this.data = value;
		}

		@Override
		protected void setValueObject(Object value) {
			if (value instanceof V3TestData)
				this.data = (V3TestData) value;
		}

		@Override
		public Class<?>[] accepts() {
			return new Class[] { V3TestData.class };
		}

		@Override
		public void toBytes(Storage ebs) {
			ebs.writeInt(data.test);
		}

		@Override
		public void fromBytes(Storage ebs) {
			if (data == null)
				data = new V3TestData(ebs.readInt());
			else
				data.test = ebs.readInt();
		}

	}

}
