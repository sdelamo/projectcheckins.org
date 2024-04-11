package org.projectcheckins.core.forms;

import io.micronaut.context.StaticMessageSource;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;

@Singleton
public class ValidationMessages extends StaticMessageSource {

    public static final String NOT_EMPTY_DAYS_MESSAGE = "You must select at least one day.";
    public static final String NOT_EMPTY_RESPONDENTS_MESSAGE = "You must select at least one respondent.";
    public static final String ONLY_ONCE_PER_DAY_MESSAGE = "You already have an answer for that question and day.";
    public static final String MEMBER_NOT_ALREADY_REGISTERED_MESSAGE = "Team member already registered.";

    @PostConstruct
    void init() {
        addMessage(NotEmptyDays.MESSAGE, NOT_EMPTY_DAYS_MESSAGE);
        addMessage(NotEmptyRespondents.MESSAGE, NOT_EMPTY_RESPONDENTS_MESSAGE);
        addMessage(OnlyOncePerDay.MESSAGE, ONLY_ONCE_PER_DAY_MESSAGE);
        addMessage(MemberNotAlreadyRegistered.MESSAGE, MEMBER_NOT_ALREADY_REGISTERED_MESSAGE);
    }
}
