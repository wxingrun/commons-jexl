package org.apache.commons.jexl3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class SwitchNumericTest extends JexlTestCase {

    public SwitchNumericTest() {
        super("SwitchNumericTest");
    }

    @Test
    public void testNumericTypes() {
        JexlEngine jexl = new JexlBuilder().safe(false).strict(true).create();
        JexlScript e = jexl.createScript("switch(x) { case 1: 'int 1'; case 1L: 'long 1'; default: 'def'; }");
        JexlContext jc = new MapContext();
        
        // Let's see what happens with Long(1)
        jc.set("x", 1L);
        assertEquals("long 1", e.execute(jc));
        
        jc.set("x", 1);
        assertEquals("int 1", e.execute(jc));
        
        jc.set("x", (short) 1);
        // Does Short(1) match anything? Probably "def" because 1 is Int and 1L is Long.
        assertEquals("def", e.execute(jc));
    }
}
