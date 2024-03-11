package org.projectcheckins.core.forms;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FullNameUtilsTest {

    @Test
    void fullname() {
        assertEquals("first last", FullNameUtils.getFullName("first", "last"));
        assertEquals("first", FullNameUtils.getFullName("first", null));
        assertEquals("last", FullNameUtils.getFullName(null, "last"));
        assertEquals("", FullNameUtils.getFullName(null, null));
    }
}