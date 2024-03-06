package org.projectcheckins.core.markdown;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotBlank;

@FunctionalInterface
public interface MarkdownRenderer {

    /**
     *
     * @param markdown Markdown to render
     * @return A rendered HTML string
     */
    @NonNull
    String render(@NonNull @NotBlank String markdown);
}
