package org.projectcheckins.annotations;

import io.micronaut.http.annotation.UriMapping;
import io.micronaut.scheduling.TaskExecutors;

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({ElementType.METHOD})
@Inherited
public @interface PostForm {
    String uri() default UriMapping.DEFAULT_URI;

    String[] rolesAllowed();

    boolean hidden() default true;

    String executesOn() default TaskExecutors.BLOCKING;
}
