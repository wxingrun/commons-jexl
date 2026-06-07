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

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Tests for null-safe member access operator '?.'
 */
class NullSafeAccessTest extends JexlTestCase {

    public static class Inner {
        private final String name;
        private final Inner child;

        public Inner(final String name, final Inner child) {
            this.name = name;
            this.child = child;
        }

        public String getName() {
            return name;
        }

        public Inner getChild() {
            return child;
        }
    }

    public static class Container {
        private final Inner inner;

        public Container(final Inner inner) {
            this.inner = inner;
        }

        public Inner getInner() {
            return inner;
        }
    }

    public NullSafeAccessTest() {
        super("NullSafeAccessTest");
    }

    @Test
    void testNullSafeAccessOnNullObject() {
        final JexlEngine engine = new JexlBuilder().strict(true).silent(false).create();
        final JexlContext ctx = new MapContext();
        ctx.set("a", null);

        final JexlScript script = engine.createScript("a?.b");
        final Object result = script.execute(ctx);
        assertNull(result);
    }

    @Test
    void testChainedNullSafeAccessOnNullObject() {
        final JexlEngine engine = new JexlBuilder().strict(true).silent(false).create();
        final JexlContext ctx = new MapContext();
        ctx.set("a", null);

        final JexlScript script = engine.createScript("a?.b?.c");
        final Object result = script.execute(ctx);
        assertNull(result);
    }

    @Test
    void testChainedNullSafeAccessOnNullIntermediate() {
        final JexlEngine engine = new JexlBuilder().strict(true).silent(false).create();
        final JexlContext ctx = new MapContext();

        final Inner inner = new Inner("hello", null);
        ctx.set("a", new Container(inner));

        final JexlScript script = engine.createScript("a.inner?.child?.name");
        final Object result = script.execute(ctx);
        assertNull(result);
    }

    @Test
    void testNullSafeAccessWithNonNullValue() {
        final JexlEngine engine = new JexlBuilder().strict(true).silent(false).create();
        final JexlContext ctx = new MapContext();

        final Inner child = new Inner("world", null);
        final Inner parent = new Inner("hello", child);
        ctx.set("a", new Container(parent));

        final JexlScript script = engine.createScript("a.inner?.child?.name");
        final Object result = script.execute(ctx);
        assertEquals("world", result);
    }

    @Test
    void testMixedSafeAndUnsafeAccessThrowsOnNull() {
        final JexlEngine engine = new JexlBuilder().strict(true).silent(false).create();
        final JexlContext ctx = new MapContext();
        ctx.set("a", null);

        final JexlScript script = engine.createScript("a?.b.c");
        assertThrows(JexlException.class, () -> script.execute(ctx));
    }

    @Test
    void testNormalAccessThrowsOnNull() {
        final JexlEngine engine = new JexlBuilder().strict(true).silent(false).create();
        final JexlContext ctx = new MapContext();
        ctx.set("a", null);

        final JexlScript script = engine.createScript("a.b");
        assertThrows(JexlException.class, () -> script.execute(ctx));
    }

    @Test
    void testNullSafeAccessWithMap() {
        final JexlEngine engine = new JexlBuilder().strict(true).silent(false).create();
        final JexlContext ctx = new MapContext();

        final Map<String, Object> map = new HashMap<>();
        map.put("key", "value");
        ctx.set("m", map);

        JexlScript script = engine.createScript("m?.key");
        Object result = script.execute(ctx);
        assertEquals("value", result);

        script = engine.createScript("m?.nonexistent");
        result = script.execute(ctx);
        assertNull(result);
    }

    @Test
    void testDeeplyNestedNullSafeAccess() {
        final JexlEngine engine = new JexlBuilder().strict(true).silent(false).create();
        final JexlContext ctx = new MapContext();

        final Inner level4 = new Inner("level4", null);
        final Inner level3 = new Inner("level3", level4);
        final Inner level2 = new Inner("level2", level3);
        final Inner level1 = new Inner("level1", level2);
        ctx.set("a", new Container(level1));

        final JexlScript script = engine.createScript("a?.inner?.child?.child?.child?.name");
        final Object result = script.execute(ctx);
        assertEquals("level4", result);
    }

    @Test
    void testNullSafeAccessOnNullMap() {
        final JexlEngine engine = new JexlBuilder().strict(true).silent(false).create();
        final JexlContext ctx = new MapContext();
        ctx.set("m", null);

        final JexlScript script = engine.createScript("m?.key");
        final Object result = script.execute(ctx);
        assertNull(result);
    }
}