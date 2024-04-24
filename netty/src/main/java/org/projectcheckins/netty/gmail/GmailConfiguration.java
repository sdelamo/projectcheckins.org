package org.projectcheckins.netty.gmail;

import io.micronaut.core.annotation.NonNull;

public interface GmailConfiguration {

    @NonNull
    String getAppSpecificPassword();
}
