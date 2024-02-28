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
