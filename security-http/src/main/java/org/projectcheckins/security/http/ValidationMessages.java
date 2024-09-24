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

package org.projectcheckins.security.http;

import io.micronaut.context.StaticMessageSource;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;

@Singleton
public class ValidationMessages extends StaticMessageSource {
    public static final String CHECK_CURRENT_PASSWORD_MESSAGE = "Current password is incorrect. Please try again.";

    @PostConstruct
    void init() {
        addMessage(CheckCurrentPassword.MESSAGE, CHECK_CURRENT_PASSWORD_MESSAGE);
    }
}
