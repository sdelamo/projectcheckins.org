// Copyright 2024 Object Computing, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

//     http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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
