package org.projectcheckins.core.markdown;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@MicronautTest(startApplication = false)
class MarkdownRendererTest {

    @Test
    void renderMarkdown(MarkdownRenderer markdownRenderer) {
        assertThat(markdownRenderer.render("This is **Sparta**"))
                .isEqualTo("<p>This is <strong>Sparta</strong></p>\n");
    }
}