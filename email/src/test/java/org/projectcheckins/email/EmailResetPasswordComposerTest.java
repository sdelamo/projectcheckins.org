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

package org.projectcheckins.email;

import io.micronaut.context.annotation.Property;
import io.micronaut.email.BodyType;
import io.micronaut.email.Contact;
import io.micronaut.email.Email;
import io.micronaut.email.MultipartBody;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Property(name = "email.sender", value = "info@projectcheckins.org")
@MicronautTest
class EmailResetPasswordComposerTest {

    @Test
    void emailInvitation(EmailResetPasswordComposer emailResetPasswordComposer) {
        EmailResetPassword resetPassword = new EmailResetPassword("sergio.delamo@softamo.com", "https://projectcheckins.example.com/security/resetPassword?token=foobar");
        assertThatThrownBy(() -> emailResetPasswordComposer.composeEmailResetPassword(null, resetPassword))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageEndingWith("composeEmailResetPassword.locale: must not be null");
        assertThatThrownBy(() -> emailResetPasswordComposer.composeEmailResetPassword(Locale.ENGLISH, null))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageEndingWith("composeEmailResetPassword.emailResetPassword: must not be null");
        assertThatThrownBy(() -> emailResetPasswordComposer.composeEmailResetPassword(Locale.ENGLISH, new EmailResetPassword(null, null)))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageEndingWith("composeEmailResetPassword.emailResetPassword.url: must not be blank");

        Email.Builder emailBuilder = emailResetPasswordComposer.composeEmailResetPassword(Locale.ENGLISH,
                resetPassword);
        assertThat(emailBuilder)
            .isNotNull()
            .extracting(Email.Builder::build)
            .hasFieldOrPropertyWithValue("subject", "Reset your password")
            .hasFieldOrPropertyWithValue("from", new Contact("info@projectcheckins.org"))
            .hasFieldOrPropertyWithValue("to", List.of(new Contact("sergio.delamo@softamo.com")))
            .extracting(Email::getBody)
            .isInstanceOf(MultipartBody.class)
            .satisfies(b -> assertThat(b.get(BodyType.TEXT)).hasValueSatisfying(x -> assertThat(x).startsWith("""
                Please, click the link below and reset your password:
                https://projectcheckins.example.com/security/resetPassword?token=foobar""")))
            .satisfies(b -> assertThat(b.get(BodyType.HTML)).hasValueSatisfying(x -> assertThat(x).startsWith("""
                Please, click the link below and reset your password:
                <a href="https://projectcheckins.example.com/security/resetPassword?token=foobar""")));
    }
}
