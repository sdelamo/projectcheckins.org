package org.projectcheckins.security;

import io.micronaut.context.event.ApplicationEvent;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.security.forms.TeamMemberSave;

import java.util.Locale;

public class InvitationSavedEvent extends ApplicationEvent {

    @NotBlank
    @Email
    private final String email;

    @NotNull
    private final Locale locale;

    @NotBlank
    private final String url;

    public InvitationSavedEvent(@NotNull TeamMemberSave form, @NotNull Locale locale, @NotBlank String url) {
        super(form);
        this.email = form.email();
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
