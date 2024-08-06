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
