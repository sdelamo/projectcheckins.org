package org.projectcheckins.email;

import io.micronaut.core.annotation.NonNull;

public interface EmailConfiguration {
    @NonNull
    String getSender();
}
