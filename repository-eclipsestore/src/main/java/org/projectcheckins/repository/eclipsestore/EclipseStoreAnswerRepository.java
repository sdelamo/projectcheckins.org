package org.projectcheckins.repository.eclipsestore;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.eclipsestore.RootProvider;
import io.micronaut.eclipsestore.annotations.StoreParams;
import io.micronaut.multitenancy.Tenant;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.projectcheckins.core.api.Answer;
import org.projectcheckins.core.idgeneration.IdGenerator;
import org.projectcheckins.core.repositories.AnswerRepository;

import static java.util.Comparator.comparing;

@Singleton
class EclipseStoreAnswerRepository implements AnswerRepository {

    private static final Comparator<AnswerEntity> CHRONOLOGICALLY = comparing(AnswerEntity::answerDate);

    private final RootProvider<Data> rootProvider;
    private final IdGenerator idGenerator;

    public EclipseStoreAnswerRepository(RootProvider<Data> rootProvider,
                                        IdGenerator idGenerator) {
        this.rootProvider = rootProvider;
        this.idGenerator = idGenerator;
    }

    @Override
    @NonNull
    public String save(@NotNull @Valid Answer answer, @Nullable Tenant tenant) {
        String id = idGenerator.generate();
        AnswerEntity entity = answerOf(id, answer);
        save(rootProvider.root().getAnswers(), entity);
        return id;
    }

    @Override
    @NonNull
    public Optional<? extends Answer> findById(@NotBlank String id, @Nullable Tenant tenant) {
        return rootProvider.root().getAnswers().stream()
                .filter(a -> a.id().equals(id))
                .findAny();
    }

    @Override
    @NonNull
    public List<? extends Answer> findByQuestionId(@NotBlank String questionId,
                                                   @Nullable Tenant tenant) {
        return rootProvider.root().getAnswers().stream()
                .filter(a -> a.questionId().equals(questionId))
                .sorted(CHRONOLOGICALLY.reversed())
                .toList();
    }

    @StoreParams("answers")
    public void save(List<AnswerEntity> answers, AnswerEntity answer) {
        answers.add(answer);
    }

    @NonNull
    private static AnswerEntity answerOf(@NonNull String id, @NonNull Answer answer) {
        AnswerEntity entity = new AnswerEntity();
        entity.id(id);
        entity.questionId(answer.questionId());
        entity.respondentId(answer.respondentId());
        entity.answerDate(answer.answerDate());
        entity.format(answer.format());
        entity.text(answer.text());
        return entity;
    }
}
