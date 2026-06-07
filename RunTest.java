import org.apache.commons.jexl3.*;
import java.lang.reflect.Method;

public class RunTest {
    public static void main(String[] args) {
        try {
            SafeNavigationTest test = new SafeNavigationTest();
            test.setUp();
            test.testSafeNavigation();
            test.tearDown();
            System.out.println("SafeNavigationTest PASSED!");
            
            PropertyAccessTest test2 = new PropertyAccessTest();
            test2.setUp();
            for (Method m : PropertyAccessTest.class.getDeclaredMethods()) {
                if (m.getName().startsWith("test") && m.getParameterCount() == 0) {
                    try {
                        m.invoke(test2);
                    } catch (Exception e) {
                        System.out.println(m.getName() + " FAILED");
                        e.printStackTrace();
                    }
                }
            }
            test2.tearDown();
            System.out.println("PropertyAccessTest completed.");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
