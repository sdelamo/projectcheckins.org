package org.projectcheckins.repository.eclipsestore;

import jakarta.validation.constraints.NotBlank;

public class QuestionEntity {
    @NotBlank
    private String id;

    @NotBlank
    private String title;

    @NotBlank
    private String schedule;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
