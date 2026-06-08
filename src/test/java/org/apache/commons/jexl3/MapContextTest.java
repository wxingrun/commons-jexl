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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

@SuppressWarnings("AssertEqualsBetweenInconvertibleTypes")
class MapContextTest {

    @Test
    void testPutAllNormal() {
        final MapContext ctx = new MapContext();
        final Map<String, Object> vars = new HashMap<>();
        vars.put("x", 1);
        vars.put("y", "hello");
        vars.put("z", true);
        ctx.putAll(vars);
        assertEquals(1, ctx.get("x"));
        assertEquals("hello", ctx.get("y"));
        assertEquals(true, ctx.get("z"));
    }

    @Test
    void testPutAllOverwritesExisting() {
        final MapContext ctx = new MapContext();
        ctx.set("a", 10);
        final Map<String, Object> vars = new HashMap<>();
        vars.put("a", 20);
        vars.put("b", 30);
        ctx.putAll(vars);
        assertEquals(20, ctx.get("a"));
        assertEquals(30, ctx.get("b"));
    }

    @Test
    void testPutAllEmptyMap() {
        final MapContext ctx = new MapContext();
        ctx.set("a", 1);
        ctx.putAll(new HashMap<>());
        assertEquals(1, ctx.get("a"));
        assertTrue(ctx.has("a"));
    }

    @Test
    void testPutAllNullThrowsNPE() {
        final MapContext ctx = new MapContext();
        assertThrows(NullPointerException.class, () -> ctx.putAll(null));
    }

    @Test
    void testPutAllWithNullValue() {
        final MapContext ctx = new MapContext();
        final Map<String, Object> vars = new HashMap<>();
        vars.put("x", null);
        ctx.putAll(vars);
        assertTrue(ctx.has("x"));
        assertNull(ctx.get("x"));
    }

    @Test
    void testPutAllConsistentWithSet() {
        final MapContext ctx1 = new MapContext();
        final MapContext ctx2 = new MapContext();
        final Map<String, Object> vars = new HashMap<>();
        vars.put("a", 1);
        vars.put("b", 2);
        vars.put("c", 3);
        ctx1.putAll(vars);
        for (Map.Entry<String, Object> entry : vars.entrySet()) {
            ctx2.set(entry.getKey(), entry.getValue());
        }
        for (String key : vars.keySet()) {
            assertEquals(ctx1.get(key), ctx2.get(key));
        }
    }

    @Test
    void testPutAllOnPrePopulatedMap() {
        final Map<String, Object> initial = new HashMap<>();
        initial.put("x", 100);
        final MapContext ctx = new MapContext(initial);
        final Map<String, Object> more = new HashMap<>();
        more.put("y", 200);
        ctx.putAll(more);
        assertEquals(100, ctx.get("x"));
        assertEquals(200, ctx.get("y"));
    }

    @Test
    void testPutAllDoesNotAffectSourceMap() {
        final MapContext ctx = new MapContext();
        final Map<String, Object> vars = new HashMap<>();
        vars.put("a", 1);
        ctx.putAll(vars);
        vars.put("b", 2);
        assertFalse(ctx.has("b"));
    }
}
