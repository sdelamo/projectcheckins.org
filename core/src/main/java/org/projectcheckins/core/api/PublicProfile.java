package org.projectcheckins.core.api;

import io.micronaut.core.annotation.NonNull;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.forms.FullNameUtils;
import org.projectcheckins.core.forms.TimeFormat;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.TimeZone;

public interface PublicProfile {

    @NotBlank String id();

    @NotBlank @Email String email();

    @NonNull
    String fullName();
}
