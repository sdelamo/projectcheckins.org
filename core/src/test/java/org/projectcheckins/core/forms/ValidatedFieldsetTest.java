package org.projectcheckins.core.forms;

import io.micronaut.views.fields.messages.Message;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ValidatedFieldsetTest {


    @Test
    void noErrors() {
        assertThat(new ValidatedFieldset() {
            @Override
            public Map<String, List<Message>> fieldErrors() {
                return null;
            }

            @Override
            public List<Message> errors() {
                return null;
            }
        }).returns(false, ValidatedFieldset::hasFieldsetErrors)
                .returns(false, ValidatedFieldset::hasErrors);
    }

    @Test
    void fieldError() {
        assertThat(new ValidatedFieldset() {
            @Override
            public Map<String, List<Message>> fieldErrors() {
                return Map.of("title", List.of(Message.of("must not be blank")));
            }

            @Override
            public List<Message> errors() {
                return null;
            }
        }).returns(true, ValidatedFieldset::hasFieldsetErrors)
                .returns(List.of(Message.of("must not be blank")), it -> it.getError("title"))
                .returns(false, ValidatedFieldset::hasErrors);
    }

    @Test
    void typeError() {
        assertThat(new ValidatedFieldset() {
            @Override
            public Map<String, List<Message>> fieldErrors() {
                return null;
            }

            @Override
            public List<Message> errors() {
                return List.of(Message.of("must not be blank"));
            }
        }).returns(false, ValidatedFieldset::hasFieldsetErrors)
                .returns(List.of(Message.of("must not be blank")), it -> it.errors())
                .returns(true, ValidatedFieldset::hasErrors);
    }
}