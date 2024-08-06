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
