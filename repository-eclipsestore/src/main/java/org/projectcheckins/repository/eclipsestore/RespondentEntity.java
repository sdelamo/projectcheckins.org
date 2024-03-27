package org.projectcheckins.repository.eclipsestore;

import jakarta.validation.constraints.NotBlank;
import org.projectcheckins.core.api.Respondent;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class RespondentEntity implements Respondent {

    @NotBlank
    private String id;

    @Override
    public String id() {
        return id;
    }

    public void id(String id) {
        this.id = id;
    }

    public static RespondentEntity toEntity(Respondent respondent) {
        final RespondentEntity entity = new RespondentEntity();
        entity.id(respondent.id());
        return entity;
    }

    public static Set<RespondentEntity> toEntities(Set<? extends Respondent> respondents) {
        return respondents.stream().map(RespondentEntity::toEntity).collect(toSet());
    }
}
