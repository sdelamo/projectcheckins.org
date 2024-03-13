package org.projectcheckins.repository.eclipsestore;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.Profile;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.forms.TimeFormat;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Introspected
public class UserEntity implements Profile {

    @NotBlank
    private String id;

    @NotBlank
    private String email;

    boolean enabled;

    @NotBlank
    private String encodedPassword;

    @NonNull
    private List<String> authorities;

    @NotNull
    private TimeZone timeZone;

    @NotNull
    private DayOfWeek firstDayOfWeek;

    @NotNull
    private LocalTime beginningOfDay;

    @NotNull
    private LocalTime endOfDay;

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
                      LocalTime beginningOfDay,
                      LocalTime endOfDay,
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
        this.beginningOfDay = beginningOfDay;
        this.endOfDay = endOfDay;
        this.timeFormat = timeFormat;
        this.format = format;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String id() {
        return id;
    }

    public void id(String id) {
        this.id = id;
    }

    public String email() {
        return email;
    }

    public void email(String email) {
        this.email = email;
    }

    public boolean enabled() {
        return enabled;
    }

    public void enabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String encodedPassword() {
        return encodedPassword;
    }

    public void encodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    public List<String> authorities() {
        return authorities;
    }

    public void authorities(List<String> authorities) {
        this.authorities = authorities;
    }

    @Override
    public TimeZone timeZone() {
        return timeZone;
    }

    public void timeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public DayOfWeek firstDayOfWeek() {
        return firstDayOfWeek;
    }

    public void firstDayOfWeek(DayOfWeek firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
    }

    @Override
    public LocalTime beginningOfDay() {
        return beginningOfDay;
    }

    public void beginningOfDay(LocalTime beginningOfDay) {
        this.beginningOfDay = beginningOfDay;
    }

    @Override
    public LocalTime endOfDay() {
        return endOfDay;
    }

    public void endOfDay(LocalTime endOfDay) {
        this.endOfDay = endOfDay;
    }

    @Override
    public TimeFormat timeFormat() {
        return timeFormat;
    }

    public void timeFormat(TimeFormat timeFormat) {
        this.timeFormat = timeFormat;
    }

    @Override
    public String firstName() {
        return firstName;
    }

    public void firstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String lastName() {
        return lastName;
    }

    public void lastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public Format format() {
        return format;
    }

    public void format(Format format) {
        this.format = format;
    }
}
