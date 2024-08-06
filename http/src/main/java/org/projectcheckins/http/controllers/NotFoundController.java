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

package org.projectcheckins.http.controllers;

import io.micronaut.http.MutableHttpResponse;
import org.projectcheckins.annotations.GetHtml;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.security.rules.SecurityRule;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

@Controller
class NotFoundController {
    private static final String PATH = "/notFound";

    @GetHtml(uri = PATH, view = "/error/404.html", rolesAllowed = SecurityRule.IS_ANONYMOUS)
    Map<String, Object> index() {
        return Collections.emptyMap();
    }


    public static <T> MutableHttpResponse<T> notFoundRedirect() {
        return HttpResponse.seeOther(URI.create(PATH));
    }
}
