package org.projectcheckins.netty.gmail;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotBlank;

@Requires(property = "gmail.app-specific-password")
@ConfigurationProperties("gmail")
class GmailConfigurationProperties implements GmailConfiguration {

    @NotBlank
    private String appSpecificPassword;


    @Override
    @NonNull
    public String getAppSpecificPassword() {
        return appSpecificPassword;
    }

    public void setAppSpecificPassword(@NonNull String appSpecificPassword) {
        this.appSpecificPassword = appSpecificPassword;
    }
}
