package org.projectcheckins.security.http;

import io.micronaut.context.StaticMessageSource;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;

@Singleton
public class ValidationMessages extends StaticMessageSource {
    public static final String CHECK_CURRENT_PASSWORD_MESSAGE = "Current password is incorrect. Please try again.";

    @PostConstruct
    void init() {
        addMessage(CheckCurrentPassword.MESSAGE, CHECK_CURRENT_PASSWORD_MESSAGE);
    }
}
