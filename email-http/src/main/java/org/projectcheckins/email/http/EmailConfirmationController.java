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

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import org.projectcheckins.email.EmailConfirmationRepository;
import org.projectcheckins.email.EmailConfirmationTokenValidator;

import java.net.URI;
import java.util.Optional;

@Controller("${" + EmailConfirmationControllerConfigurationProperties.PREFIX + ".path:"+ EmailConfirmationControllerConfigurationProperties.DEFAULT_PATH +"}")
class EmailConfirmationController {

    private final EmailConfirmationControllerConfiguration emailConfirmationControllerConfiguration;
    private final EmailConfirmationTokenValidator emailConfirmationTokenValidator;
    private final EmailConfirmationRepository emailConfirmationRepository;

    EmailConfirmationController(EmailConfirmationControllerConfiguration emailConfirmationControllerConfiguration,
                                EmailConfirmationTokenValidator emailConfirmationTokenValidator,
                                EmailConfirmationRepository emailConfirmationRepository) {
        this.emailConfirmationControllerConfiguration = emailConfirmationControllerConfiguration;
        this.emailConfirmationTokenValidator = emailConfirmationTokenValidator;
        this.emailConfirmationRepository = emailConfirmationRepository;
    }

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Get
    HttpResponse<?> confirm(@QueryValue String token) {
        Optional<Authentication> authenticationOptional = emailConfirmationTokenValidator.validate(token);
        if (authenticationOptional.isEmpty()) {
            return HttpResponse.seeOther(URI.create(emailConfirmationControllerConfiguration.getFailureRedirect()));
        }
        Authentication authentication = authenticationOptional.get();
        emailConfirmationRepository.enableByEmail(authentication.getName());
        return HttpResponse.seeOther(URI.create(emailConfirmationControllerConfiguration.getSuccessfulRedirect()));
    }
}
