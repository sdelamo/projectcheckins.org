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
import org.projectcheckins.core.api.Respondent;

import java.time.ZonedDateTime;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class RespondentEntity implements Respondent {

    @NotBlank
    private String id;

    @NotNull
    private ZonedDateTime nextExecution;

    @Override
    public String id() {
        return id;
    }

    public void id(@NotBlank String id) {
        this.id = id;
    }

    @Override
    @NotNull
    public ZonedDateTime nextExecution() {
        return nextExecution;
    }

    public void nextExecution(@NotNull ZonedDateTime nextExecution) {
        this.nextExecution = nextExecution;
    }

    public static RespondentEntity toEntity(Respondent respondent) {
        final RespondentEntity entity = new RespondentEntity();
        entity.id(respondent.id());
        entity.nextExecution(respondent.nextExecution());
        return entity;
    }

    public static Set<RespondentEntity> toEntities(Set<? extends Respondent> respondents) {
        return respondents.stream().map(RespondentEntity::toEntity).collect(toSet());
    }
}
