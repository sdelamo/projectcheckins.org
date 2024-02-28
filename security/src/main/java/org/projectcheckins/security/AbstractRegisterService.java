package org.projectcheckins.security;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotBlank;

import java.util.Collections;
import java.util.List;

public abstract class AbstractRegisterService implements RegisterService {

    private final PasswordEncoder passwordEncoder;

    protected AbstractRegisterService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String register(@NonNull @NotBlank String username,
                           @NonNull @NotBlank String rawPassword) throws UserAlreadyExistsException {
        return register(username, rawPassword, Collections.emptyList());
    }

    @Override
    public String register(@NonNull @NotBlank String username,
                           @NonNull @NotBlank String rawPassword,
                           @NonNull List<String> authorities) throws UserAlreadyExistsException {
        final String encodedPassword = passwordEncoder.encode(rawPassword);
        return register(new UserSave(username, encodedPassword, authorities));
    }
}
