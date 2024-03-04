package org.projectcheckins.email.http;

import io.micronaut.core.annotation.NonNull;

public interface EmailConfirmationControllerConfiguration {

    /**
     * @return The path to send the user to to confirm its email. Typically, a link in an email.
     */
    @NonNull
    String getPath();

    /**
     * @return The path to redirect to after a successfully confirming a user's email
     */
    @NonNull
    String getSuccessfulRedirect();

    /**
     * @return The path to redirect to after an unsuccessful confirmation.
     */
    @NonNull
    String getFailureRedirect();
}
