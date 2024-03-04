package org.projectcheckins.email.http;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.NonNull;

@ConfigurationProperties(EmailConfirmationControllerConfigurationProperties.PREFIX)
public class EmailConfirmationControllerConfigurationProperties implements EmailConfirmationControllerConfiguration {
    public static final String PREFIX = "email.confirmation";
    public static final String DEFAULT_PATH = "/email/confirm";
    public static final String DEFAULT_SUCCESSFUL_REDIRECT = "/security/login";
    public static final String DEFAULT_FAILURE_REDIRECT = "/";
    private String path = DEFAULT_PATH;
    private String successfulRedirect = DEFAULT_SUCCESSFUL_REDIRECT;
    private String failureRedirect = DEFAULT_FAILURE_REDIRECT;

    @Override
    @NonNull
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    @NonNull
    public String getSuccessfulRedirect() {
        return successfulRedirect;
    }

    @Override
    public String getFailureRedirect() {
        return failureRedirect;
    }

    public void setFailureRedirect(String failureRedirect) {
        this.failureRedirect = failureRedirect;
    }

    public void setSuccessfulRedirect(String successfulRedirect) {
        this.successfulRedirect = successfulRedirect;
    }
}
