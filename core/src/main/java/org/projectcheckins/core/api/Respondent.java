package org.projectcheckins.core.api;

import jakarta.validation.constraints.NotBlank;

public interface Respondent {

    @NotBlank String id();
}
