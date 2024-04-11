package org.projectcheckins.core.forms;

import jakarta.inject.Singleton;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.projectcheckins.security.UserFetcher;

@Singleton
class MemberNotAlreadyRegisteredValidator implements ConstraintValidator<MemberNotAlreadyRegistered, TeamMemberSave> {

    private UserFetcher userFetcher;

    MemberNotAlreadyRegisteredValidator(UserFetcher userFetcher) {
        this.userFetcher = userFetcher;
    }

    @Override
    public boolean isValid(TeamMemberSave value, ConstraintValidatorContext context) {
        return userFetcher.findByEmail(value.email()).isEmpty();
    }
}
