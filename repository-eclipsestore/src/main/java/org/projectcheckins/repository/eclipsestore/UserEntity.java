package org.projectcheckins.repository.eclipsestore;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.forms.TimeFormat;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Introspected
public class UserEntity {
    private @NotBlank String id;

    @NotBlank
    private String email;

    boolean enabled;
    @NotBlank
    private String encodedPassword;
    @NonNull List<String> authorities = new ArrayList<>();

    @NotNull
    private TimeZone timeZone;

    @NotNull
    private DayOfWeek firstDayOfWeek;

    @NotNull
    private TimeFormat timeFormat;

    @NotNull
    private Format format;

    @Nullable
    private String firstName;

    @Nullable
    private String lastName;

    public UserEntity(String id,
                      String email,
                      String encodedPassword,
                      List<String> authorities,
                      TimeZone timeZone,
                      DayOfWeek firstDayOfWeek,
                      TimeFormat timeFormat,
                      Format format,
                      String firstName,
                      String lastName) {
        this.id = id;
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.authorities = authorities;
        this.timeZone = timeZone;
        this.firstDayOfWeek = firstDayOfWeek;
        this.timeFormat = timeFormat;
        this.format = format;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public DayOfWeek getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    public void setFirstDayOfWeek(DayOfWeek firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
    }

    public TimeFormat getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(TimeFormat timeFormat) {
        this.timeFormat = timeFormat;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }
}
