package org.projectcheckins.security;

import io.micronaut.context.event.ApplicationEvent;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Locale;

public class PasswordResetEvent extends ApplicationEvent {

    @NotBlank
    @Email
    private final String email;

    @NotNull
    private final Locale locale;

    @NotBlank
    private final String url;

    public PasswordResetEvent(@NotBlank @Email String email, @NotNull Locale locale, @NotBlank String url) {
        super(email);
        this.email = email;
        this.locale = locale;
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getUrl() {
        return url;
    }
}
