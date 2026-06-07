package org.apache.commons.jexl3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

public class SafeNavigationTest extends JexlTestCase {

    public SafeNavigationTest() {
        super("SafeNavigationTest");
    }

    public static class Foo {
        public Bar bar;
        public Bar getBar() { return bar; }
        public void setBar(Bar bar) { this.bar = bar; }
    }

    public static class Bar {
        public String name;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    @Test
    public void testSafeNavigation() throws Exception {
        JexlEngine jexl = new JexlBuilder().strict(true).safe(false).create();
        JexlContext context = new MapContext();
        
        Foo foo = new Foo();
        context.set("foo", foo);
        
        // foo.bar is null
        JexlExpression e = jexl.createExpression("foo?.bar?.name");
        Object result = e.evaluate(context);
        assertNull(result);

        // chained safe access on null object
        e = jexl.createExpression("nullObject?.some?.property");
        result = e.evaluate(context);
        assertNull(result);
        
        // normal property access
        Bar bar = new Bar();
        bar.setName("jexl");
        foo.setBar(bar);
        e = jexl.createExpression("foo?.bar?.name");
        result = e.evaluate(context);
        assertEquals("jexl", result);
        
        // mixed access
        e = jexl.createExpression("foo.bar?.name");
        result = e.evaluate(context);
        assertEquals("jexl", result);
    }
}
