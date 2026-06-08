/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.jexl3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

@SuppressWarnings({"AssertEqualsBetweenInconvertibleTypes"})
class NullSafeOperatorTest extends JexlTestCase {

    public static class Inner {
        private final String value;

        Inner(final String v) {
            this.value = v;
        }

        public String getValue() {
            return value;
        }

        public String concat(final String s) {
            return value + s;
        }
    }

    public static class Outer {
        private final Inner inner;

        Outer(final Inner i) {
            this.inner = i;
        }

        public Inner getInner() {
            return inner;
        }

        public String getName() {
            return "outer";
        }
    }

    NullSafeOperatorTest() {
        super("NullSafeOperatorTest");
    }

    @Test
    void testChainedNullSafeAccess() {
        final JexlEngine jexl = new JexlBuilder().strict(true).safe(false).create();
        final JexlContext ctx = new MapContext();
        ctx.set("a", null);
        assertNull(jexl.createScript("a?.b?.c").execute(ctx));
        assertNull(jexl.createScript("a?.inner?.value").execute(ctx));
        ctx.set("a", new Outer(null));
        assertNull(jexl.createScript("a?.inner?.value").execute(ctx));
        assertNull(jexl.createScript("a?.getInner()?.getValue()").execute(ctx));
        ctx.set("a", new Outer(new Inner("hello")));
        assertEquals("hello", jexl.createScript("a?.inner?.value").execute(ctx));
        assertEquals("hello", jexl.createScript("a?.getInner()?.getValue()").execute(ctx));
    }

    @Test
    void testNullSafeWithRegularAccess() {
        final JexlEngine jexl = new JexlBuilder().strict(true).safe(false).create();
        final JexlContext ctx = new MapContext();
        ctx.set("a", null);
        assertNull(jexl.createScript("a?.b.c").execute(ctx));
        assertNull(jexl.createScript("a?.b.method()").execute(ctx));
        assertNull(jexl.createScript("a?.inner.value").execute(ctx));
        assertNull(jexl.createScript("a?.inner.getValue()").execute(ctx));
        ctx.set("a", new Outer(new Inner("world")));
        assertEquals("world", jexl.createScript("a?.inner.value").execute(ctx));
        assertEquals("world", jexl.createScript("a?.inner.getValue()").execute(ctx));
    }

    @Test
    void testNullSafeMethodCall() {
        final JexlEngine jexl = new JexlBuilder().strict(true).safe(false).create();
        final JexlContext ctx = new MapContext();
        ctx.set("a", null);
        assertNull(jexl.createScript("a?.getName()").execute(ctx));
        assertNull(jexl.createScript("a?.concat('x')").execute(ctx));
        ctx.set("a", new Outer(new Inner("test")));
        assertEquals("outer", jexl.createScript("a?.getName()").execute(ctx));
        assertEquals("testx", jexl.createScript("a?.inner?.concat('x')").execute(ctx));
    }

    @Test
    void testNullSafeArrayAccess() {
        final JexlEngine jexl = new JexlBuilder().strict(true).safe(false).create();
        final JexlContext ctx = new MapContext();
        ctx.set("a", null);
        assertNull(jexl.createScript("a?[0]").execute(ctx));
        assertNull(jexl.createScript("a?[0]?.length").execute(ctx));
        assertNull(jexl.createScript("a?.inner?[0]").execute(ctx));
        final String[] arr = {"hello", "world"};
        ctx.set("a", arr);
        assertEquals("hello", jexl.createScript("a?[0]").execute(ctx));
        assertEquals(5, jexl.createScript("a?[0]?.length()").execute(ctx));
    }

    @Test
    void testNullSafeVsRegularAccess() {
        final JexlEngine jexl = new JexlBuilder().strict(true).safe(false).create();
        final JexlContext ctx = new MapContext();
        ctx.set("a", null);
        assertThrows(JexlException.class, () -> jexl.createScript("a.b").execute(ctx));
        assertThrows(JexlException.class, () -> jexl.createScript("a.method()").execute(ctx));
        assertNull(jexl.createScript("a?.b").execute(ctx));
        assertNull(jexl.createScript("a?.method()").execute(ctx));
        ctx.set("a", new Outer(new Inner("safe")));
        assertEquals("safe", jexl.createScript("a.inner.value").execute(ctx));
        assertEquals("safe", jexl.createScript("a?.inner?.value").execute(ctx));
    }

    @Test
    void testNullSafeWithNullMiddle() {
        final JexlEngine jexl = new JexlBuilder().strict(true).safe(false).create();
        final JexlContext ctx = new MapContext();
        ctx.set("a", new Outer(null));
        assertNull(jexl.createScript("a?.inner?.value").execute(ctx));
        assertNull(jexl.createScript("a?.inner?.getValue()").execute(ctx));
        assertNull(jexl.createScript("a?.inner?.concat('x')").execute(ctx));
        assertNull(jexl.createScript("a.inner?.value").execute(ctx));
        assertNull(jexl.createScript("a.inner?.getValue()").execute(ctx));
        ctx.set("a", new Outer(new Inner("mid")));
        assertEquals("mid", jexl.createScript("a?.inner?.value").execute(ctx));
        assertEquals("mid", jexl.createScript("a.inner?.value").execute(ctx));
    }

    @Test
    void testNullSafeWithNonNullValue() {
        final JexlEngine jexl = new JexlBuilder().strict(true).safe(false).create();
        final JexlContext ctx = new MapContext();
        ctx.set("a", new Outer(new Inner("val")));
        assertEquals("val", jexl.createScript("a?.inner?.value").execute(ctx));
        assertEquals("val", jexl.createScript("a?.inner.value").execute(ctx));
        assertEquals("val", jexl.createScript("a.inner?.value").execute(ctx));
        assertEquals("outer", jexl.createScript("a?.getName()").execute(ctx));
        assertEquals("valx", jexl.createScript("a?.inner?.concat('x')").execute(ctx));
    }
}
