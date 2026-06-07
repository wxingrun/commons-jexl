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

import org.apache.commons.jexl3.internal.Dumper;
import org.junit.jupiter.api.Test;

class NullSafePropertyAccessTest extends JexlTestCase {
    public static class ChainNode {
        private final ChainNode child;
        private final String value;

        public ChainNode(final ChainNode child, final String value) {
            this.child = child;
            this.value = value;
        }

        public ChainNode getChild() {
            return child;
        }

        public String getValue() {
            return value;
        }
    }

    public NullSafePropertyAccessTest() {
        super("NullSafePropertyAccessTest");
    }

    @Test
    void testNullSafePropertyAstNode() {
        final JexlScript script = JEXL.createScript("root?.child?.value", "root");
        final String dump = Dumper.toString(script);
        assertTrue(dump.contains("ASTNullSafePropertyAccess@child"));
        assertTrue(dump.contains("ASTNullSafePropertyAccess@value"));
    }

    @Test
    void testNullSafePropertyChainReturnsNullForNullRoot() {
        final JexlScript script = JEXL.createScript("root?.child?.value", "root");
        assertNull(script.execute(null, (Object) null));
    }

    @Test
    void testNullSafePropertyChainReturnsNullForNullIntermediate() {
        final JexlScript script = JEXL.createScript("root?.child?.value", "root");
        final ChainNode root = new ChainNode(null, "root");
        assertNull(script.execute(null, root));
    }

    @Test
    void testNullSafePropertyChainReturnsLeafValue() {
        final JexlScript script = JEXL.createScript("root?.child?.value", "root");
        final ChainNode leaf = new ChainNode(null, "done");
        final ChainNode root = new ChainNode(leaf, "root");
        assertEquals("done", script.execute(null, root));
    }

    @Test
    void testUnsafeSegmentAfterNullSafePropertyStillFails() {
        final JexlEngine jexl = new JexlBuilder().strict(true).safe(false).silent(false).create();
        final JexlScript script = jexl.createScript("root?.child.value", "root");
        final ChainNode root = new ChainNode(null, "root");
        assertThrows(JexlException.class, () -> script.execute(null, root));
    }

    @Test
    void testPlainPropertyAccessStillThrowsOnNullRoot() {
        final JexlEngine jexl = new JexlBuilder().strict(true).safe(false).silent(false).create();
        final JexlScript script = jexl.createScript("root.child.value", "root");
        assertThrows(JexlException.class, () -> script.execute(null, (Object) null));
    }
}
