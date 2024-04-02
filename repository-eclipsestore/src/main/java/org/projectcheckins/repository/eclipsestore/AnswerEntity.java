package org.projectcheckins.repository.eclipsestore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.projectcheckins.core.api.Answer;
import org.projectcheckins.core.forms.Format;

import java.time.LocalDate;

public class AnswerEntity implements Answer {

    @NotBlank
    private String id;

    @NotBlank
    private String questionId;

    @NotBlank
    private String respondentId;

    @NotNull
    @PastOrPresent
    private LocalDate answerDate;

    @NotNull
    private Format format;

    @NotBlank
    private String text;

    @Override
    public String id() {
        return id;
    }

    public void id(String id) {
        this.id = id;
    }

    @Override
    public String questionId() {
        return questionId;
    }

    public void questionId(String questionId) {
        this.questionId = questionId;
    }

    @Override
    public String respondentId() {
        return respondentId;
    }

    public void respondentId(String respondentId) {
        this.respondentId = respondentId;
    }

    @Override
    public LocalDate answerDate() {
        return answerDate;
    }

    public void answerDate(LocalDate answerDate) {
        this.answerDate = answerDate;
    }

    @Override
    public Format format() {
        return format;
    }

    public void format(Format format) {
        this.format = format;
    }

    @Override
    public String text() {
        return text;
    }

    public void text(String text) {
        this.text = text;
    }
}
