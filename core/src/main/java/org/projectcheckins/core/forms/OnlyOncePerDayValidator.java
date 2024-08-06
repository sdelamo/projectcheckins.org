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

package org.projectcheckins.core.forms;

import jakarta.inject.Singleton;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.projectcheckins.core.repositories.AnswerRepository;

import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Stream;

@Singleton
class OnlyOncePerDayValidator implements ConstraintValidator<OnlyOncePerDay, AnswerForm> {

    private AnswerRepository answerRepository;

    OnlyOncePerDayValidator(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Override
    public boolean isValid(AnswerForm value, ConstraintValidatorContext context) {
        // these fields are validated separately, but will make this validator trip if null
        final String questionId = value.questionId();
        final String respondentId = value.respondentId();
        final LocalDate answerDate = value.answerDate();
        final boolean invalid = Stream.of(questionId, respondentId, answerDate).filter(Objects::nonNull).count() < 3;
        return invalid || answerRepository.findByQuestionIdAndRespondentId(questionId, respondentId).stream()
                .noneMatch(a -> answerDate.equals(a.answerDate()));
    }
}
