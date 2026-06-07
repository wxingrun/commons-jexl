import org.apache.commons.jexl3.*;

public class TestSafeAccess {
    public static void main(String[] args) {
        JexlEngine jexl = new JexlBuilder().strict(true).safe(false).create();
        JexlContext context = new MapContext();
        try {
            JexlExpression e = jexl.createExpression("a?.b?.c");
            Object result = e.evaluate(context);
            System.out.println("Result: " + result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
