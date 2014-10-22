package hp.test;

public class TMain {
	public static void main(String[] args) {
		TestStream ts1 = TestStream.getTest();
		ts1.setName("jason");
		TestStream ts2 = TestStream.getTest();
		ts2.setName("0539");

		ts1.printInfo();
		ts2.printInfo();

		if (ts1 == ts2) {
			System.out.println("创建的是同一个实例");
		} else {
			System.out.println("创建的不是同一个实例");
		}
	}
}

class TestStream {
	String name = null;
	private static TestStream ts1 = null;
	private TestStream() {
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public static TestStream getTest() {
		if (ts1 == null) {
			ts1 = new TestStream();
		}
		return ts1;
	}
	public void printInfo() {
		System.out.println("the name is " + name);
	}

}