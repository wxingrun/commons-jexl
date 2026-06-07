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

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for MapContext.
 */
public class MapContextTest {

    @Test
    public void testPutAll() {
        final MapContext context = new MapContext();
        final Map<String, Object> vars = new HashMap<>();
        vars.put("foo", "bar");
        vars.put("num", 42);

        context.putAll(vars);

        assertEquals("bar", context.get("foo"));
        assertEquals(42, context.get("num"));
        assertTrue(context.has("foo"));
        assertTrue(context.has("num"));
    }

    @Test
    public void testPutAllEmpty() {
        final MapContext context = new MapContext();
        context.set("existing", "value");

        final Map<String, Object> vars = new HashMap<>();
        context.putAll(vars);

        assertEquals("value", context.get("existing"));
        assertFalse(context.has("nonexisting"));
    }

    @Test
    public void testPutAllNull() {
        final MapContext context = new MapContext();
        assertThrows(NullPointerException.class, () -> context.putAll(null));
    }
}
