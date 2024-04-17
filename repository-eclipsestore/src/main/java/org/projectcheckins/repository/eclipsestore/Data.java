package org.projectcheckins.repository.eclipsestore;

import io.micronaut.core.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Data {
    @NonNull
    private List<QuestionEntity> questions = new ArrayList<>();
    @NonNull
    private List<AnswerEntity> answers = new ArrayList<>();
    @NonNull
    private List<UserEntity> users = new ArrayList<>();
    @NonNull
    private List<TeamInvitationEntity> invitations = new ArrayList<>();

    public List<QuestionEntity> getQuestions() {
        return questions;
    }

    public List<AnswerEntity> getAnswers() {
        return answers;
    }

    public List<UserEntity> getUsers() {
        return users;
    }
    public List<TeamInvitationEntity> getInvitations() {
        return invitations;
    }
}
