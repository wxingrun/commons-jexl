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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class MapContextTest {
    @Test
    void testPutAll() {
        final MapContext context = new MapContext();
        context.set("existing", 1);
        final Map<String, Object> variables = new LinkedHashMap<>();
        variables.put("existing", 2);
        variables.put("added", "value");
        variables.put("nullValue", null);

        context.putAll(variables);

        assertEquals(2, context.get("existing"));
        assertEquals("value", context.get("added"));
        assertTrue(context.has("nullValue"));
        assertNull(context.get("nullValue"));
    }

    @Test
    void testPutAllEmptyMap() {
        final MapContext context = new MapContext();
        context.set("existing", 42);

        context.putAll(Collections.emptyMap());

        assertEquals(42, context.get("existing"));
        assertTrue(context.has("existing"));
    }

    @Test
    void testPutAllNullMap() {
        final MapContext context = new MapContext();

        assertThrows(NullPointerException.class, () -> context.putAll(null));
    }
}
