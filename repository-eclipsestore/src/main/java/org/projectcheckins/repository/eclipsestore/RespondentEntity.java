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
