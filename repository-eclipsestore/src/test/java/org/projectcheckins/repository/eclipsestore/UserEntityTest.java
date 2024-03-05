package org.projectcheckins.repository.eclipsestore;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {

    @Test
    void settersAndGetters() {
        UserEntity user = new UserEntity("id", "email@projectcheckins.org", "password", Collections.emptyList());
        assertEquals("id", user.getId());
        assertEquals("email@projectcheckins.org", user.getEmail());
        assertEquals("password", user.getEncodedPassword());
        assertFalse(user.isEnabled());
        assertTrue(user.authorities.isEmpty());

        user.setId("newId");
        user.setEmail("newEmail@projectcheckins.org");
        user.setEnabled(true);
        user.setEncodedPassword("newPassword");
        user.setAuthorities(Collections.singletonList("ROLE_USER"));
        assertEquals("newId", user.getId());
        assertEquals("newEmail@projectcheckins.org", user.getEmail());
        assertTrue(user.isEnabled());
        assertEquals("newPassword", user.getEncodedPassword());
        assertFalse(user.authorities.isEmpty());
    }

}