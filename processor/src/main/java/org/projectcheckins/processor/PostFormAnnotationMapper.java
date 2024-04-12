package org.projectcheckins.processor;

import io.micronaut.views.turbo.http.TurboMediaType;
import org.projectcheckins.annotations.PostForm;
import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.inject.annotation.TypedAnnotationMapper;
import io.micronaut.inject.visitor.VisitorContext;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.swagger.v3.oas.annotations.Hidden;

import java.util.ArrayList;
import java.util.List;

public class PostFormAnnotationMapper implements TypedAnnotationMapper<PostForm> {
    private static final String MEMBER_VALUE = "value";
    private static final String MEMBER_URI = "uri";
    private static final String MEMBER_ROLESALLOWED  = "rolesAllowed";
    private static final String MEMBER_EXECUTES_ON = "executesOn";
    private static final String MEMBER_HIDDEN = "hidden";

    @Override
    public Class<PostForm> annotationType() {
        return PostForm.class;
    }

    @Override
    public List<AnnotationValue<?>> map(AnnotationValue<PostForm> annotation, VisitorContext visitorContext) {
        List<AnnotationValue<?>> result = new ArrayList<>();

        result.add(AnnotationValue.builder(Produces.class)
                        .member(MEMBER_VALUE, MediaType.TEXT_HTML, TurboMediaType.TURBO_STREAM)
                .build());

        result.add(AnnotationValue.builder(Consumes.class).member(MEMBER_VALUE, MediaType.APPLICATION_FORM_URLENCODED).build());

        annotation.stringValue(MEMBER_URI).ifPresent(uri ->
                result.add(AnnotationValue.builder(Post.class).member(MEMBER_URI, uri).build()));

        String[] rolesAllowed = annotation.stringValues(MEMBER_ROLESALLOWED);
        result.add(AnnotationValue.builder(Secured.class).member(MEMBER_VALUE, rolesAllowed).build());

        result.add(AnnotationValue.builder(ExecuteOn.class).member(MEMBER_VALUE, annotation.stringValue(MEMBER_EXECUTES_ON).orElse(TaskExecutors.BLOCKING)).build());

        if (annotation.booleanValue(MEMBER_HIDDEN).orElse(true)) {
            result.add(AnnotationValue.builder(Hidden.class).build());
        }
        return result;
    }
}
