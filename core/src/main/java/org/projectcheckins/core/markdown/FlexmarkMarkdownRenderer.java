package org.projectcheckins.core.markdown;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import jakarta.inject.Singleton;

@Singleton
class FlexmarkMarkdownRenderer implements MarkdownRenderer {
    private final Parser parser;
    private final HtmlRenderer renderer;
    FlexmarkMarkdownRenderer() {
        MutableDataSet options = new MutableDataSet();
        options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");
        this.parser = Parser.builder(options).build();
        this.renderer = HtmlRenderer.builder(options).build();
    }

    @Override
    public String render(String markdown) {
        Node document = parser.parse(markdown);
        return renderer.render(document);
    }
}
