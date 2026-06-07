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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class MapContextTest {

    @Test
    public void testPutAll() {
        final MapContext context = new MapContext();

        final Map<String, Object> variables = new HashMap<>();
        variables.put("a", 1);
        variables.put("b", "hello");
        variables.put("c", 3.14);

        context.putAll(variables);

        assertEquals(1, context.get("a"));
        assertEquals("hello", context.get("b"));
        assertEquals(3.14, context.get("c"));
    }

    @Test
    public void testPutAllEmpty() {
        final MapContext context = new MapContext();
        context.set("x", 42);

        context.putAll(Collections.<String, Object>emptyMap());

        assertEquals(42, context.get("x"));
        assertNull(context.get("y"));
    }

    @Test
    public void testPutAllNull() {
        final MapContext context = new MapContext();

        assertThrows(NullPointerException.class, () -> context.putAll(null));
    }

    @Test
    public void testPutAllOverwrite() {
        final MapContext context = new MapContext();
        context.set("key", "original");

        final Map<String, Object> variables = new HashMap<>();
        variables.put("key", "updated");
        variables.put("newKey", "newValue");

        context.putAll(variables);

        assertEquals("updated", context.get("key"));
        assertEquals("newValue", context.get("newKey"));
    }

    @Test
    public void testPutAllPreservesContext() {
        final Map<String, Object> backingMap = new HashMap<>();
        final MapContext context = new MapContext(backingMap);

        final Map<String, Object> variables = new HashMap<>();
        variables.put("a", 1);
        variables.put("b", 2);

        context.putAll(variables);

        assertEquals(1, backingMap.get("a"));
        assertEquals(2, backingMap.get("b"));
        assertEquals(2, backingMap.size());
    }
}