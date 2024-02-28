package org.projectcheckins.processor;

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

import java.util.List;

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

        List<AnnotationValue<?>> annotations = mapper.map(value, null);
        assertNotNull(annotations);
        assertEquals(6, annotations.size());
        assertTrue(annotations.stream().anyMatch(ann ->
                AnnotationValue.builder(ExecuteOn.class).member("value", TaskExecutors.BLOCKING).build().equals(ann)));
        assertTrue(annotations.stream().anyMatch(ann ->
                AnnotationValue.builder(View.class).member("value", "/organizer/show.html").build().equals(ann)));
        assertTrue(annotations.stream().anyMatch(ann ->
                AnnotationValue.builder(Get.class).member("uri", "/{id}/show").build().equals(ann)));
        assertTrue(annotations.stream().anyMatch(ann ->
                AnnotationValue.builder(Produces.class).member("value", MediaType.TEXT_HTML).build().equals(ann)));
        assertTrue(annotations.stream().anyMatch(ann ->
                AnnotationValue.builder(Secured.class).member("value", "ROLE_ORGANIZER_READ", "ROLE_ORGANIZER_WRITE", "ROLE_ADMIN").build().equals(ann)));
        assertTrue(annotations.stream().anyMatch(ann ->
                AnnotationValue.builder(Hidden.class).build().equals(ann)));

        value = AnnotationValue.builder(GetHtml.class)
                .member("uri", "/{id}/show")
                .member("rolesAllowed", "ROLE_ORGANIZER_READ", "ROLE_ORGANIZER_WRITE", "ROLE_ADMIN")
                .build();

        annotations = mapper.map(value, null);
        assertNotNull(annotations);
        assertEquals(5, annotations.size());
        assertTrue(annotations.stream().anyMatch(ann ->
                AnnotationValue.builder(ExecuteOn.class).member("value", TaskExecutors.BLOCKING).build().equals(ann)));
        assertTrue(annotations.stream().anyMatch(ann ->
                AnnotationValue.builder(Get.class).member("uri", "/{id}/show").build().equals(ann)));
        assertTrue(annotations.stream().anyMatch(ann ->
                AnnotationValue.builder(Produces.class).member("value", MediaType.TEXT_HTML).build().equals(ann)));
        assertTrue(annotations.stream().anyMatch(ann ->
                AnnotationValue.builder(Secured.class).member("value", "ROLE_ORGANIZER_READ", "ROLE_ORGANIZER_WRITE", "ROLE_ADMIN").build().equals(ann)));
        assertTrue(annotations.stream().anyMatch(ann ->
                AnnotationValue.builder(Hidden.class).build().equals(ann)));

    }
}
