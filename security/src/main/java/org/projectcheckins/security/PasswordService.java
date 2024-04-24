package org.projectcheckins.security;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.uri.UriBuilder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Locale;
import java.util.Optional;

public interface PasswordService {

    String TOKEN_QUERY_PARAM = "token";

    void updatePassword(@NonNull @NotBlank String userId,
                        @NonNull @NotBlank String newRawPassword);

    @NonNull
    Optional<@Email String> resetPassword(@NonNull @NotBlank String token,
                                          @NonNull @NotBlank String newRawPassword);

    void sendResetInstructions(@NonNull @NotBlank @Email String email,
                               @NonNull Locale locale,
                               @NonNull UriBuilder resetPasswordUri);
}
