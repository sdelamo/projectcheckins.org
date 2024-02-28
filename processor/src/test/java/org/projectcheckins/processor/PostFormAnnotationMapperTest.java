package org.projectcheckins.processor;

import static org.assertj.core.api.Assertions.assertThat;

import org.projectcheckins.annotations.PostForm;
import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.swagger.v3.oas.annotations.Hidden;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PostFormAnnotationMapperTest {

    @Test
    void storeParamsIsMappedToStore() {
        PostFormAnnotationMapper mapper = new PostFormAnnotationMapper();
        assertEquals(PostForm.class, mapper.annotationType());

        AnnotationValue<PostForm> value = AnnotationValue.builder(PostForm.class)
                .member("uri", "/{id}/update")
                .member("rolesAllowed", "ROLE_ORGANIZER_WRITE", "ROLE_ADMIN")
                .build();

        assertThat(mapper.map(value, null))
            .isNotNull()
            .hasSize(6)
            .containsExactlyInAnyOrder(
                AnnotationValue.builder(Produces.class).member("value", MediaType.TEXT_HTML).build(),
                AnnotationValue.builder(ExecuteOn.class).member("value", TaskExecutors.BLOCKING).build(),
                AnnotationValue.builder(Post.class).member("uri", "/{id}/update").build(),
                AnnotationValue.builder(Consumes.class).member("value", MediaType.APPLICATION_FORM_URLENCODED).build(),
                AnnotationValue.builder(Secured.class).member("value", "ROLE_ORGANIZER_WRITE", "ROLE_ADMIN").build(),
                AnnotationValue.builder(Hidden.class).build()
            );
    }
}
