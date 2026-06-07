package org.apache.commons.jexl3;

import org.junit.jupiter.api.Test;
import java.math.BigInteger;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JexlArithmeticBugTest {
    @Test
    public void testBigIntegerPlusLong() {
        JexlEngine jexl = new JexlBuilder().create();
        JexlContext jc = new MapContext();
        JexlExpression e = jexl.createExpression("a + 1L");
        jc.set("a", new BigInteger("9223372036854775808")); // Long.MAX_VALUE + 1
        Object result = e.evaluate(jc);
        assertEquals(new BigInteger("9223372036854775809"), result);
    }

    @Test
    public void testBigIntegerMinusLong() {
        JexlEngine jexl = new JexlBuilder().create();
        JexlContext jc = new MapContext();
        JexlExpression e = jexl.createExpression("a - 1L");
        jc.set("a", new BigInteger("-9223372036854775809")); // Long.MIN_VALUE - 1
        Object result = e.evaluate(jc);
        assertEquals(new BigInteger("-9223372036854775810"), result);
    }

    @Test
    public void testBigIntegerMultiplyLong() {
        JexlEngine jexl = new JexlBuilder().create();
        JexlContext jc = new MapContext();
        JexlExpression e = jexl.createExpression("a * 2L");
        jc.set("a", new BigInteger("9223372036854775808")); // Long.MAX_VALUE + 1
        Object result = e.evaluate(jc);
        assertEquals(new BigInteger("18446744073709551616"), result);
    }

    @Test
    public void testBigIntegerDivideLong() {
        JexlEngine jexl = new JexlBuilder().create();
        JexlContext jc = new MapContext();
        JexlExpression e = jexl.createExpression("a / 2L");
        jc.set("a", new BigInteger("18446744073709551616")); 
        Object result = e.evaluate(jc);
        assertEquals(new BigInteger("9223372036854775808"), result);
    }

    @Test
    public void testBigIntegerModLong() {
        JexlEngine jexl = new JexlBuilder().create();
        JexlContext jc = new MapContext();
        JexlExpression e = jexl.createExpression("a % 2L");
        jc.set("a", new BigInteger("18446744073709551617")); 
        Object result = e.evaluate(jc);
        assertEquals(1, result); // 1 will be narrowed to Integer
    }
}