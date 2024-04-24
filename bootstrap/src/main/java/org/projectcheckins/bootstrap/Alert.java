package org.projectcheckins.bootstrap;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.views.fields.messages.Message;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * @see <a href="https://getbootstrap.com/docs/5.3/components/alerts/">Alerts</a>
 */
@Serdeable
public record Alert(@NonNull @NotNull @Valid Message message, @NonNull @NotNull AlertVariant variant, boolean dismissible) {
    public Alert(@NonNull String defaultMessage, @NonNull AlertVariant variant, boolean dismissible) {
        this(Message.of(defaultMessage), variant, dismissible);
    }

    @NonNull
    public static Alert danger(@NonNull Message message) {
        return danger(message, true);
    }

    @NonNull
    public static Alert danger(@NonNull Message message, boolean dismissible) {
        return new Alert(message, AlertVariant.DANGER, dismissible);
    }

    @NonNull
    public static Alert info(@NonNull Message message) {
        return info(message, true);
    }

    @NonNull
    public static Alert info(@NonNull Message message, boolean dismissible) {
        return new Alert(message, AlertVariant.INFO, dismissible);
    }
}
