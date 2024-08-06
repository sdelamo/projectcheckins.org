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

package org.projectcheckins.bootstrap;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.views.fields.messages.Message;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * @see <a href="https://getbootstrap.com/docs/5.3/components/alerts/">Alerts</a>
 */
@Serdeable
public record Alert(@NonNull @NotNull @Valid Message message, @NonNull @NotNull AlertVariant variant, boolean dismissible) {
    public Alert(@NonNull String defaultMessage, @NonNull AlertVariant variant, boolean dismissible) {
        this(Message.of(defaultMessage), variant, dismissible);
    }

    @NonNull
    public static Alert danger(@NonNull Message message) {
        return danger(message, true);
    }

    @NonNull
    public static Alert danger(@NonNull Message message, boolean dismissible) {
        return new Alert(message, AlertVariant.DANGER, dismissible);
    }

    @NonNull
    public static Alert info(@NonNull Message message) {
        return info(message, true);
    }

    @NonNull
    public static Alert info(@NonNull Message message, boolean dismissible) {
        return new Alert(message, AlertVariant.INFO, dismissible);
    }
}
