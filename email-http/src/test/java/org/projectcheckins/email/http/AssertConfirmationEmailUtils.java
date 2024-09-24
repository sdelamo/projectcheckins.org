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

package org.projectcheckins.email.http;

import io.micronaut.email.BodyType;
import io.micronaut.email.Email;
import io.micronaut.email.MultipartBody;

import static org.assertj.core.api.Assertions.assertThat;

public final class AssertConfirmationEmailUtils {
    private AssertConfirmationEmailUtils() {

    }

    public static void assertConfirmationEmail(String recipient, Email email) {
        assertThat(email.getSubject())
                .isEqualTo("Confirm email address for your Project Check-ins account");
        assertThat(email.getFrom().getEmail())
                .isEqualTo("info@projectcheckins.org");
        assertThat(email.getTo())
                .hasSize(1);
        assertThat(email.getTo().stream().findFirst().get().getEmail())
                .isEqualTo(recipient);

        assertThat(email.getBody())
                .isInstanceOf(MultipartBody.class);
        MultipartBody multipartBody = (MultipartBody) email.getBody();
        assertThat(multipartBody.get(BodyType.TEXT))
                .isNotEmpty();

        assertThat(multipartBody.get(BodyType.TEXT).get()).startsWith("""
                To confirm your email address, please click the link below:
                https://projectcheckins.example.com/email/confirm?token=""");
        assertThat(multipartBody.get(BodyType.HTML))
                .isNotEmpty();
        assertThat(multipartBody.get(BodyType.HTML).get()).startsWith("""
                To confirm your email address, please click the link below:
                <a href="https://projectcheckins.example.com/email/confirm?token=""");
    }
}
