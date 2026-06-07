import org.apache.commons.jexl3.*;
import java.util.Map;
import java.util.HashMap;

public class SwitchTest {
    public static void main(String[] args) {
        JexlEngine jexl = new JexlBuilder().create();
        JexlContext jc = new MapContext();
        
        String script = "switch(x) { case 1: 'int 1'; case 1L: 'long 1'; case '1': 'string 1'; default: 'def'; }";
        JexlScript e = jexl.createScript(script);
        
        jc.set("x", 1);
        System.out.println("x=1 (int) -> " + e.execute(jc));
        
        jc.set("x", 1L);
        System.out.println("x=1L (long) -> " + e.execute(jc));
        
        jc.set("x", "1");
        System.out.println("x='1' (string) -> " + e.execute(jc));
        
        // Let's also check fallthrough
        String script2 = "switch(x) { case 1: 'int 1'; case 2: 'int 2'; }";
        JexlScript e2 = jexl.createScript(script2);
        jc.set("x", 1);
        System.out.println("fallthrough test (switch expr) -> " + e2.execute(jc));
    }
}
