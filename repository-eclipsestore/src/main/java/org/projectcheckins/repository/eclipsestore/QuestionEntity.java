package org.projectcheckins.repository.eclipsestore;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.Question;
import org.projectcheckins.core.forms.HowOften;
import org.projectcheckins.core.forms.TimeOfDay;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

public class QuestionEntity implements Question {
    @NotBlank
    private String id;

    @NotBlank
    private String title;

    @NotNull
    private HowOften howOften;

    @NotEmpty
    private Set<DayOfWeek> days;

    @NotNull
    private TimeOfDay timeOfDay;

    @NotNull
    private LocalTime fixedTime;

    @Override
    public String id() {
        return id;
    }

    public void id(String id) {
        this.id = id;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    @NonNull
    public HowOften howOften() {
        return howOften;
    }

    @Override
    @NotEmpty
    public Set<DayOfWeek> days() {
        return days;
    }

    @Override
    @NonNull
    public TimeOfDay timeOfDay() {
        return timeOfDay;
    }

    @Override
    @NonNull
    public LocalTime fixedTime() {
        return fixedTime;
    }

    public void title(String title) {
        this.title = title;
    }

    public void howOften(@NonNull HowOften howOften) {
        this.howOften = howOften;
    }

    public void days(@NotEmpty Set<DayOfWeek> days) {
        this.days = days;
    }

    public void timeOfDay(@NonNull TimeOfDay timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public void fixedTime(@NonNull LocalTime fixedTime) {
        this.fixedTime = fixedTime;
    }
}
