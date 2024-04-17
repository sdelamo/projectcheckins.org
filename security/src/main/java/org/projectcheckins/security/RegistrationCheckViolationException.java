package org.projectcheckins.security;

public class RegistrationCheckViolationException extends Exception {
    private final RegistrationCheckViolation violation;
    public RegistrationCheckViolationException(RegistrationCheckViolation violation) {
        this.violation = violation;
    }

    public RegistrationCheckViolation getViolation() {
        return violation;
    }
}
