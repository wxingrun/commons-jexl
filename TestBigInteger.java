import java.math.BigInteger;
import org.apache.commons.jexl3.JexlArithmetic;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlExpression;

public class TestBigInteger {
    public static void main(String[] args) {
        try {
            JexlArithmetic arithmetic = new JexlArithmetic(false);
            
            // Test BigInteger + Long
            BigInteger bi1 = new BigInteger("9223372036854775808");
            Long l1 = 1L;
            Object result1 = arithmetic.add(bi1, l1);
            System.out.println("Test 1 (BigInteger + Long): " + bi1 + " + " + l1 + " = " + result1);
            System.out.println("Expected: 9223372036854775809, Actual: " + result1);
            
            // Test Long + BigInteger
            Object result2 = arithmetic.add(l1, bi1);
            System.out.println("\nTest 2 (Long + BigInteger): " + l1 + " + " + bi1 + " = " + result2);
            System.out.println("Expected: 9223372036854775809, Actual: " + result2);
            
            // Test BigInteger - Long
            BigInteger bi2 = new BigInteger("100");
            Long l2 = 50L;
            Object result3 = arithmetic.subtract(bi2, l2);
            System.out.println("\nTest 3 (BigInteger - Long): " + bi2 + " - " + l2 + " = " + result3);
            
            // Test Long - BigInteger
            Object result4 = arithmetic.subtract(l2, bi2);
            System.out.println("\nTest 4 (Long - BigInteger): " + l2 + " - " + bi2 + " = " + result4);
            
            // Test BigInteger * Long
            BigInteger bi3 = new BigInteger("2");
            Long l3 = 100L;
            Object result5 = arithmetic.multiply(bi3, l3);
            System.out.println("\nTest 5 (BigInteger * Long): " + bi3 + " * " + l3 + " = " + result5);
            
            // Test BigInteger / Long
            BigInteger bi4 = new BigInteger("100");
            Long l4 = 2L;
            Object result6 = arithmetic.divide(bi4, l4);
            System.out.println("\nTest 6 (BigInteger / Long): " + bi4 + " / " + l4 + " = " + result6);
            
            // Test BigInteger % Long
            BigInteger bi5 = new BigInteger("10");
            Long l5 = 3L;
            Object result7 = arithmetic.mod(bi5, l5);
            System.out.println("\nTest 7 (BigInteger % Long): " + bi5 + " % " + l5 + " = " + result7);
            
            // Test with negative numbers
            BigInteger bi6 = new BigInteger("-10");
            Long l6 = 5L;
            Object result8 = arithmetic.add(bi6, l6);
            System.out.println("\nTest 8 (Negative BigInteger + Long): " + bi6 + " + " + l6 + " = " + result8);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
