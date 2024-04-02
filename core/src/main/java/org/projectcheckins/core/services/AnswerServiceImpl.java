package org.projectcheckins.core.services;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.Answer;
import org.projectcheckins.core.forms.*;
import org.projectcheckins.core.repositories.AnswerRepository;

import java.util.List;

@Singleton
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;

    public AnswerServiceImpl(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Override
    @NonNull
    public String save(@NotNull Authentication authentication,
                       @NotNull @Valid AnswerSave answerSave,
                       @Nullable Tenant tenant) {
        final AnswerRecord answer = new AnswerRecord(
                null,
                answerSave.questionId(),
                authentication.getName(),
                answerSave.answerDate(),
                answerSave.format(),
                answerSave.text()
        );
        return answerRepository.save(answer, tenant);
    }

    @Override
    @NonNull
    public List<? extends Answer> findByQuestionId(@NotBlank String questionId,
                                                   @Nullable Tenant tenant) {
        return answerRepository.findByQuestionId(questionId, tenant);
    }
}
