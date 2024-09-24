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

package org.projectcheckins.core.services;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.ClientAuthentication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.api.Answer;
import org.projectcheckins.core.api.AnswerView;
import org.projectcheckins.core.api.Profile;
import org.projectcheckins.core.forms.*;
import org.projectcheckins.core.repositories.AnswerRepository;
import org.projectcheckins.core.repositories.ProfileRepository;
import org.projectcheckins.core.repositories.SecondaryAnswerRepository;
import org.projectcheckins.core.repositories.SecondaryProfileRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;

@Property(name = "spec.name", value = "AnswerServiceImplTest")
@MicronautTest(startApplication = false)
class AnswerServiceImplTest {

    static final Profile USER_1 = new ProfileRecord(
            "user1",
            "user1@email.com",
            TimeZone.getDefault(),
            DayOfWeek.MONDAY,
            LocalTime.of(9, 0),
            LocalTime.of(16, 30),
            TimeFormat.TWENTY_FOUR_HOUR_CLOCK,
            Format.MARKDOWN,
            "Code",
            "Monkey",
            true
            );

    static final Profile USER_2 = new ProfileRecord(
            "user2",
            "user2@email.com",
            TimeZone.getDefault(),
            DayOfWeek.SUNDAY,
            LocalTime.of(9, 0),
            LocalTime.of(16, 30),
            TimeFormat.TWELVE_HOUR_CLOCK,
            Format.WYSIWYG,
            null,
            null,
            false
            );

    static final Answer ANSWER_1 = new AnswerRecord("answer1", "question1", USER_1.id(), LocalDate.of(2024, 1, 1), Format.MARKDOWN, "Lorem *ipsum*.");
    static final Answer ANSWER_2 = new AnswerRecord("answer2", "question1", USER_2.id(), LocalDate.of(2024, 1, 2), Format.WYSIWYG, "Hello <strong>world</strong>.");

    @Inject
    AnswerServiceImpl answerService;

    @Inject
    AnswerRepository answerRepository;

    @Inject
    ProfileRepository profileRepository;

    @Test
    void testSave() {
        final Authentication auth = new ClientAuthentication(USER_1.id(), null);
        final String questionId = "question1";
        final AnswerSave answerSave = new AnswerSave(questionId, LocalDate.now(), Format.MARKDOWN, "Lorem ipsum");
        assertThat(answerService.save(auth, answerSave, null))
                .isNotBlank();
        assertThat(answerService.findByQuestionId(questionId, auth, null))
                .isNotEmpty();
    }

    @Test
    void testUpdate() {
        final Authentication auth = new ClientAuthentication(USER_1.id(), null);
        final String questionId = "question1";
        final String text = "Foo";
        final LocalDate now = LocalDate.now();
        final AnswerSave answerSave = new AnswerSave(questionId, now, Format.MARKDOWN, text);
        final String id = answerService.save(auth, answerSave, null);
        final String updatedText = "Bar";
        final AnswerUpdate answerUpdate = new AnswerUpdateRecord(now, Format.MARKDOWN, updatedText);
        assertThatCode(() -> answerService.update(auth, questionId, id, answerUpdate, null))
                .doesNotThrowAnyException();
        assertThat(answerService.findById(id, auth, null))
                .hasValueSatisfying(a -> assertThat(a.answer()).hasFieldOrPropertyWithValue("text", updatedText));
    }

    @Test
    void testShow() {
        final Authentication auth1 = new ClientAuthentication(USER_1.id(), null);

        final String answerId1 = answerRepository.save(ANSWER_1, null);
        final Optional<? extends AnswerView> optionalAnswerView1 = answerService.findById(answerId1, auth1, null);
        assertThat(optionalAnswerView1).isNotEmpty();
        final AnswerView answerView1 = optionalAnswerView1.get();
        assertThat(answerView1)
                .hasFieldOrPropertyWithValue("isEditable", true)
                .extracting("html", STRING).contains("Lorem <em>ipsum</em>.");
        assertThat(answerView1.respondent())
                .hasFieldOrPropertyWithValue("id", USER_1.id());
        assertThat(answerView1.answer())
                .hasFieldOrPropertyWithValue("id", answerId1)
                .hasFieldOrPropertyWithValue("questionId", ANSWER_1.questionId())
                .hasFieldOrPropertyWithValue("respondentId", ANSWER_1.respondentId())
                .hasFieldOrPropertyWithValue("answerDate", ANSWER_1.answerDate())
                .hasFieldOrPropertyWithValue("format", ANSWER_1.format())
                .hasFieldOrPropertyWithValue("text", ANSWER_1.text());

        final String answerId2 = answerRepository.save(ANSWER_2, null);
        final Optional<? extends AnswerView> optionalAnswerView2 = answerService.findById(answerId2, auth1, null);
        assertThat(optionalAnswerView2).isNotEmpty();
        final AnswerView answerView2 = optionalAnswerView2.get();
        assertThat(answerView2)
                .hasFieldOrPropertyWithValue("isEditable", false)
                .hasFieldOrPropertyWithValue("html", "Hello <strong>world</strong>.");
        assertThat(answerView2.respondent())
                .hasFieldOrPropertyWithValue("id", ANSWER_2.respondentId());
        assertThat(answerView2.answer())
                .hasFieldOrPropertyWithValue("id", answerId2)
                .hasFieldOrPropertyWithValue("questionId", ANSWER_2.questionId())
                .hasFieldOrPropertyWithValue("respondentId", ANSWER_2.respondentId())
                .hasFieldOrPropertyWithValue("answerDate", ANSWER_2.answerDate())
                .hasFieldOrPropertyWithValue("format", ANSWER_2.format())
                .hasFieldOrPropertyWithValue("text", ANSWER_2.text());
    }

    @Test
    void testAnswerSummary() {
        final AnswerView view1 = new AnswerViewRecord(ANSWER_1, USER_1, "Lorem <em>ipsum</em>.", true);
        assertThat(answerService.getAnswerSummary(view1, Locale.ENGLISH))
                .isEqualTo("Code Monkey's answer for January 1, 2024");

        final AnswerView view2 = new AnswerViewRecord(ANSWER_2, USER_2, "Hello <strong>world</strong>.", false);
        assertThat(answerService.getAnswerSummary(view2, Locale.ENGLISH))
                .isEqualTo("user2@email.com's answer for January 2, 2024");
    }

    @Requires(property = "spec.name", value = "AnswerServiceImplTest")
    @Singleton
    @Replaces(AnswerRepository.class)
    static class AnswerRepositoryMock extends SecondaryAnswerRepository {

        Map<String, Answer> answers = new HashMap<>();

        @Override
        @NotBlank
        public String save(@NotNull @Valid Answer answer, @Nullable Tenant tenant) {
            final String id = UUID.randomUUID().toString();
            answers.put(id, new AnswerRecord(id, answer.questionId(), answer.respondentId(), answer.answerDate(), answer.format(), answer.text()));
            return id;
        }

        @Override
        public void update(@NotNull @Valid Answer answer, @Nullable Tenant tenant) {
            answers.put(answer.id(), answer);
        }

        @Override
        @NonNull
        public Optional<? extends Answer> findById(@NotBlank String id,
                                               @Nullable Tenant tenant) {
            return Optional.ofNullable(answers.get(id));
        }

        @Override
        @NonNull
        public List<? extends Answer> findByQuestionId(@NotBlank String questionId,
                                                       @Nullable Tenant tenant) {
            return answers.values().stream().filter(a -> a.questionId().equals(questionId)).toList();
        }
    }

    @Requires(property = "spec.name", value = "AnswerServiceImplTest")
    @Singleton
    @Replaces(ProfileRepository.class)
    static class ProfileRepositoryMock extends SecondaryProfileRepository {
        @Override
        public List<? extends Profile> list(Tenant tenant) {
            return Collections.emptyList();
        }

        @Override
        public Optional<? extends Profile> findById(String id, Tenant tenant) {
            return Optional.ofNullable(switch (id) {
                case "user1" -> USER_1;
                case "user2" -> USER_2;
                default -> null;
            });
        }
    }
}
