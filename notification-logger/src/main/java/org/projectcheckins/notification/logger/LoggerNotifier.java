package org.projectcheckins.notification.logger;

import jakarta.inject.Singleton;
import org.projectcheckins.core.api.Profile;
import org.projectcheckins.core.api.Question;
import org.projectcheckins.notification.Notifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logs notifications about questions to specific user profiles.
 */
@Singleton
public class LoggerNotifier implements Notifier {

    private static final Logger LOG = LoggerFactory.getLogger(LoggerNotifier.class);

    /**
     * Logs a notification about a question to a user profile.
     *
     * @param question The question.
     * @param profile  The user profile.
     */
    @Override
    public void notify(Question question, Profile profile) {
        LOG.info("Asking user: {} question: {}", profile.fullName(), question.title());
    }
}
