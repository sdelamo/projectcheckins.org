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

package org.projectcheckins.security;

import io.micronaut.context.event.ApplicationEvent;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Locale;

public class PasswordResetEvent extends ApplicationEvent {

    @NotBlank
    @Email
    private final String email;

    @NotNull
    private final Locale locale;

    @NotBlank
    private final String url;

    public PasswordResetEvent(@NotBlank @Email String email, @NotNull Locale locale, @NotBlank String url) {
        super(email);
        this.email = email;
        this.locale = locale;
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getUrl() {
        return url;
    }
}
