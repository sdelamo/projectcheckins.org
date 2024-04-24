package org.projectcheckins.security.constraints;

import io.micronaut.context.StaticMessageSource;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;

@Singleton
class SecurityConstraintsMessages extends StaticMessageSource {
    private static final String PASSWORD_MATCH_MESSAGE = "Passwords do not match";
    private static final String UNIQUE_MESSAGE = "Invitation already exists";
    private static final String USER_DOES_NOT_EXIST_MESSAGE = "User already exists";
    private static final String INVALID_TOKEN_MESSAGE = "Token is invalid or already expired";

    private static final String MESSAGE_SUFFIX = ".message";

    SecurityConstraintsMessages() {
    }

    @PostConstruct
    void init() {
        addMessage(PasswordMatch.class.getName() + MESSAGE_SUFFIX, PASSWORD_MATCH_MESSAGE);
        addMessage(UniqueInvitation.class.getName() + MESSAGE_SUFFIX, UNIQUE_MESSAGE);
        addMessage(UserDoesNotExist.class.getName() + MESSAGE_SUFFIX, USER_DOES_NOT_EXIST_MESSAGE);
        addMessage(ValidToken.class.getName() + MESSAGE_SUFFIX, INVALID_TOKEN_MESSAGE);
    }
}
