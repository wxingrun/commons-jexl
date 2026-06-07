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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class MapContextTest {

    @Test
    void testPutAllWithValidMap() {
        final MapContext context = new MapContext();
        
        final Map<String, Object> variables = new HashMap<>();
        variables.put("a", 1);
        variables.put("b", "test");
        variables.put("c", true);
        
        context.putAll(variables);
        
        assertEquals(1, context.get("a"));
        assertEquals("test", context.get("b"));
        assertEquals(true, context.get("c"));
    }

    @Test
    void testPutAllWithEmptyMap() {
        final MapContext context = new MapContext();
        context.set("existing", "value");
        
        final Map<String, Object> emptyMap = Collections.emptyMap();
        context.putAll(emptyMap);
        
        assertEquals("value", context.get("existing"));
    }

    @Test
    void testPutAllWithNullMap() {
        final MapContext context = new MapContext();
        
        assertThrows(NullPointerException.class, () -> {
            context.putAll(null);
        });
    }

    @Test
    void testPutAllOverwritesExistingValues() {
        final MapContext context = new MapContext();
        context.set("a", 1);
        context.set("b", 2);
        
        final Map<String, Object> variables = new HashMap<>();
        variables.put("a", 100);
        variables.put("c", 3);
        
        context.putAll(variables);
        
        assertEquals(100, context.get("a"));
        assertEquals(2, context.get("b"));
        assertEquals(3, context.get("c"));
    }

    @Test
    void testPutAllWithCustomMap() {
        final Map<String, Object> customMap = new HashMap<>();
        final MapContext context = new MapContext(customMap);
        
        final Map<String, Object> variables = new HashMap<>();
        variables.put("x", "hello");
        variables.put("y", 42);
        
        context.putAll(variables);
        
        assertEquals("hello", context.get("x"));
        assertEquals(42, context.get("y"));
    }
}
