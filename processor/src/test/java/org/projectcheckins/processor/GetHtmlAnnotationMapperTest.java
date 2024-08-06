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

package org.projectcheckins.processor;

import static org.assertj.core.api.Assertions.assertThat;

import org.projectcheckins.annotations.GetHtml;
import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.views.View;
import io.swagger.v3.oas.annotations.Hidden;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GetHtmlAnnotationMapperTest {

    @Test
    void storeParamsIsMappedToStore() {
        GetHtmlAnnotationMapper mapper = new GetHtmlAnnotationMapper();
        assertEquals(GetHtml.class, mapper.annotationType());

        AnnotationValue<GetHtml> value = AnnotationValue.builder(GetHtml.class)
                .member("uri", "/{id}/show")
                .member("rolesAllowed", "ROLE_ORGANIZER_READ", "ROLE_ORGANIZER_WRITE", "ROLE_ADMIN")
                .member("view", "/organizer/show.html")
                .build();

        assertThat(mapper.map(value, null))
            .isNotNull()
            .hasSize(6)
            .containsExactlyInAnyOrder(
                AnnotationValue.builder(ExecuteOn.class).member("value", TaskExecutors.BLOCKING).build(),
                AnnotationValue.builder(View.class).member("value", "/organizer/show.html").build(),
                AnnotationValue.builder(Get.class).member("uri", "/{id}/show").build(),
                AnnotationValue.builder(Produces.class).member("value", MediaType.TEXT_HTML).build(),
                AnnotationValue.builder(Secured.class).member("value", "ROLE_ORGANIZER_READ", "ROLE_ORGANIZER_WRITE", "ROLE_ADMIN").build(),
                AnnotationValue.builder(Hidden.class).build()
            );

        value = AnnotationValue.builder(GetHtml.class)
                .member("uri", "/{id}/show")
                .member("rolesAllowed", "ROLE_ORGANIZER_READ", "ROLE_ORGANIZER_WRITE", "ROLE_ADMIN")
                .build();

        assertThat(mapper.map(value, null))
            .isNotNull()
            .hasSize(5)
            .containsExactlyInAnyOrder(
                AnnotationValue.builder(ExecuteOn.class).member("value", TaskExecutors.BLOCKING).build(),
                AnnotationValue.builder(Get.class).member("uri", "/{id}/show").build(),
                AnnotationValue.builder(Produces.class).member("value", MediaType.TEXT_HTML).build(),
                AnnotationValue.builder(Secured.class).member("value", "ROLE_ORGANIZER_READ", "ROLE_ORGANIZER_WRITE", "ROLE_ADMIN").build(),
                AnnotationValue.builder(Hidden.class).build()
            );
    }
}
