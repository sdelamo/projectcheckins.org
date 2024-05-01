package org.projectcheckins.security;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.security.authentication.Authentication;
import jakarta.validation.constraints.NotBlank;

import java.util.Collection;

public record Role(@NonNull @NotBlank String name) {

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final Role ADMIN = new Role(ROLE_ADMIN);

    public boolean matches(Collection<String> roles) {
        return roles.contains(name);
    }

    public static boolean isAdmin(Authentication authentication) {
        return ADMIN.matches(authentication.getRoles());
    }
}
