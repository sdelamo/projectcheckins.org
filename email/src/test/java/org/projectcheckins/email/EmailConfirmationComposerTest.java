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
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@Property(name = "email.sender", value = "info@projectcheckins.org")
@MicronautTest
class EmailConfirmationComposerTest {

    @Test
    void emailConfirmation(EmailConfirmationComposer emailConfirmationComposer) {
        Email.Builder emailBuilder = emailConfirmationComposer.composeEmailConfirmation(Locale.ENGLISH,
                new EmailConfirmation("https://projectcheckins.example.com/email/confirm", "sergio.delamo@softamo.com"));
        assertThat(emailBuilder)
            .isNotNull()
            .extracting(Email.Builder::build)
            .hasFieldOrPropertyWithValue("subject", "Confirm email address for your Project Check-ins account")
            .hasFieldOrPropertyWithValue("from", new Contact("info@projectcheckins.org"))
            .hasFieldOrPropertyWithValue("to", List.of(new Contact("sergio.delamo@softamo.com")))
            .extracting(Email::getBody)
            .isInstanceOf(MultipartBody.class)
            .satisfies(b -> assertThat(b.get(BodyType.TEXT)).hasValueSatisfying(x -> assertThat(x).startsWith("""
                To confirm your email address, please click the link below:
                https://projectcheckins.example.com/email/confirm?token=""")))
            .satisfies(b -> assertThat(b.get(BodyType.HTML)).hasValueSatisfying(x -> assertThat(x).startsWith("""
                To confirm your email address, please click the link below:
                <a href="https://projectcheckins.example.com/email/confirm?token=""")));
    }
}