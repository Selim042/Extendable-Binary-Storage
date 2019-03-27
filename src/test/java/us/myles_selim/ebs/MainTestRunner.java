package us.myles_selim.ebs;

import java.lang.reflect.Method;

import org.reflections.Reflections;

public class MainTestRunner {

	public static void main(String... args) {
		boolean successful = true;

		Reflections ref = new Reflections("us.myles_selim.ebs");
		for (Class<?> cl : ref.getTypesAnnotatedWith(TestClass.class)) {
			System.out.println("----[ RUNNING " + cl.getSimpleName() + " ]----");
			try {
				Method m = cl.getDeclaredMethod("main", String[].class);
				m.invoke(null, (Object) new String[0]);
			} catch (Exception e) {
				System.out.println("----[ CAUGHT " + e.getClass().getSimpleName() + " ]----");
				e.printStackTrace();
				successful = false;
			}
		}

		if (successful)
			System.out.println("\n----------\nAll tests were successful!");
		else
			System.out.println("\n----------\nAt least one test failed...");
	}

}
