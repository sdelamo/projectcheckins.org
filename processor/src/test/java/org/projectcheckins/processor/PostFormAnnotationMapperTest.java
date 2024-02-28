package org.projectcheckins.processor;

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

import java.util.List;

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

        List<AnnotationValue<?>> annotations = mapper.map(value, null);
        assertNotNull(annotations);
        assertEquals(6, annotations.size());
        assertTrue(annotations.stream().anyMatch(ann ->
                AnnotationValue.builder(Produces.class).member("value", MediaType.TEXT_HTML).build().equals(ann)));
        assertTrue(annotations.stream().anyMatch(ann ->
                AnnotationValue.builder(ExecuteOn.class).member("value", TaskExecutors.BLOCKING).build().equals(ann)));
        assertTrue(annotations.stream().anyMatch(ann ->
                AnnotationValue.builder(Post.class).member("uri", "/{id}/update").build().equals(ann)));
        assertTrue(annotations.stream().anyMatch(ann ->
                AnnotationValue.builder(Consumes.class).member("value", MediaType.APPLICATION_FORM_URLENCODED).build().equals(ann)));
        assertTrue(annotations.stream().anyMatch(ann ->
                AnnotationValue.builder(Secured.class).member("value", "ROLE_ORGANIZER_WRITE", "ROLE_ADMIN").build().equals(ann)));
        assertTrue(annotations.stream().anyMatch(ann ->
                AnnotationValue.builder(Hidden.class).build().equals(ann)));

    }
}
