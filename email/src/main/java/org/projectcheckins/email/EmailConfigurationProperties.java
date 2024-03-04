package org.projectcheckins.email;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Requires(property = "email.sender")
@ConfigurationProperties("email")
class EmailConfigurationProperties implements EmailConfiguration {
    @NonNull
    @NotBlank
    @Email
    private String sender;

    @Override
    @NonNull
    public String getSender() {
        return sender;
    }

    public void setSender(@NonNull String sender) {
        this.sender = sender;
    }
}