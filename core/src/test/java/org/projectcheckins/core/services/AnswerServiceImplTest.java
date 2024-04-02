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
import org.projectcheckins.core.forms.AnswerSave;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.repositories.AnswerRepository;
import org.projectcheckins.core.repositories.SecondaryAnswerRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Property(name = "spec.name", value = "AnswerServiceImplTest")
@MicronautTest(startApplication = false)
class AnswerServiceImplTest {

    @Inject
    AnswerServiceImpl answerService;

    @Inject
    AnswerRepository answerRepository;

    @Test
    void testSave() {
        final Authentication auth = new ClientAuthentication("user1", null);
        final String questionId = "question1";
        final AnswerSave answerSave = new AnswerSave(questionId, LocalDate.now(), Format.MARKDOWN, "Lorem ipsum");
        assertThat(answerService.save(auth, answerSave, null))
                .isNotBlank();
        assertThat(answerService.findByQuestionId(questionId, null))
                .isNotEmpty();
    }

    @Requires(property = "spec.name", value = "AnswerServiceImplTest")
    @Singleton
    @Replaces(AnswerRepository.class)
    static class AnswerRepositoryMock extends SecondaryAnswerRepository {

        List<Answer> answers = new ArrayList<>();

        @Override
        @NotBlank
        public String save(@NotNull @Valid Answer answer, @Nullable Tenant tenant) {
            answers.add(answer);
            return "xxx";
        }

        @Override
        @NonNull
        public List<? extends Answer> findByQuestionId(@NotBlank String questionId,
                                                       @Nullable Tenant tenant) {
            return answers.stream().filter(a -> a.questionId().equals(questionId)).toList();
        }
    }
}
