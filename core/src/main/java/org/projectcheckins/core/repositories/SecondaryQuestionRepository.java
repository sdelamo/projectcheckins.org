package org.projectcheckins.core.repositories;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Secondary;
import io.micronaut.context.env.Environment;
import io.micronaut.multitenancy.Tenant;
import jakarta.inject.Singleton;
import org.projectcheckins.annotations.Generated;
import org.projectcheckins.core.api.Question;

import java.util.List;
import java.util.Optional;

@Generated // "ignore for jacoco"
@Requires(env = Environment.TEST)
@Singleton
@Secondary
public class SecondaryQuestionRepository implements QuestionRepository {
    @Override
    public String save(Question questionSave, Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Optional<? extends Question> findById(String id, Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void update(Question questionUpdate, Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<? extends Question> findAll(Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteById(String id, Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
