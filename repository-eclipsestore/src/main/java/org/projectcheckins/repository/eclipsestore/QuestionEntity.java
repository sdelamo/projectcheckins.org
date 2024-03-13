package org.projectcheckins.repository.eclipsestore;

import jakarta.validation.constraints.NotBlank;
import org.projectcheckins.core.api.Question;

public class QuestionEntity implements Question {
    @NotBlank
    private String id;

    @NotBlank
    private String title;

    @NotBlank
    private String schedule;

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

    public void title(String title) {
        this.title = title;
    }

    @Override
    public String schedule() {
        return schedule;
    }

    public void schedule(String schedule) {
        this.schedule = schedule;
    }
}
