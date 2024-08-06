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

package org.projectcheckins.notification;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.Profile;
import org.projectcheckins.core.api.Question;

/**
 * Sends notifications about questions to specific user profiles.
 */
public interface Notifier {

    /**
     * Sends a notifications about a question to a user profile.
     *
     * @param question The question.
     * @param profile  The user profile.
     */
    void notify(@NotNull @Valid Question question, @NotNull @Valid Profile profile);
}
