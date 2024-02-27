package org.projectcheckins.repository.eclipsestore;

import io.micronaut.core.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Data {
    @NonNull
    private List<QuestionEntity> questions = new ArrayList<>();

    public List<QuestionEntity> getQuestions() {
        return questions;
    }
}
