package org.projectcheckins.security.constraints;

import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.validation.validator.constraints.ConstraintValidator;
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext;
import jakarta.inject.Singleton;
import org.projectcheckins.security.TeamInvitation;
import org.projectcheckins.security.UserAlreadyExistsRegistrationCheck;

@Singleton
class UserDoesNotExistValidator implements ConstraintValidator<UserDoesNotExist, TeamInvitation> {
    private final UserAlreadyExistsRegistrationCheck userAlreadyExistsRegistrationCheck;

    UserDoesNotExistValidator(UserAlreadyExistsRegistrationCheck userAlreadyExistsRegistrationCheck) {
        this.userAlreadyExistsRegistrationCheck = userAlreadyExistsRegistrationCheck;
    }

    @Override
    public boolean isValid(@Nullable TeamInvitation invitation,
                           @NonNull AnnotationValue<UserDoesNotExist> annotationMetadata,
                           @NonNull ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(invitation.email())) {
            return true;
        }
        return userAlreadyExistsRegistrationCheck.validate(invitation.email(), invitation.tenant()).isEmpty();
    }
}
