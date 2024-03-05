package org.projectcheckins.security.constraints;

import io.micronaut.context.StaticMessageSource;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;

@Singleton
public class PasswordMatchMessages extends StaticMessageSource {

    public static final String PASSWORD_MATCH_MESSAGE = "Passwords do not match";

    private static final String MESSAGE_SUFFIX = ".message";

    public PasswordMatchMessages() {
    }

    @PostConstruct
    void init() {
        addMessage(PasswordMatch.class.getName() + MESSAGE_SUFFIX, PASSWORD_MATCH_MESSAGE);
    }
}
