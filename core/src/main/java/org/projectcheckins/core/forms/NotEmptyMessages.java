package org.projectcheckins.core.forms;

import io.micronaut.context.StaticMessageSource;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;

@Singleton
public class NotEmptyMessages extends StaticMessageSource {

    public static final String NOT_EMPTY_DAYS_MESSAGE = "You must select at least one day.";

    private static final String MESSAGE_SUFFIX = ".message";

    public NotEmptyMessages() {
    }

    @PostConstruct
    void init() {
        addMessage(NotEmptyDays.class.getName() + MESSAGE_SUFFIX, NotEmptyMessages.NOT_EMPTY_DAYS_MESSAGE);
    }
}
