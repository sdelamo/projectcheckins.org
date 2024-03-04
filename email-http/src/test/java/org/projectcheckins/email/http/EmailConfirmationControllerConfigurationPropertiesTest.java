package org.projectcheckins.email.http;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailConfirmationControllerConfigurationPropertiesTest {

    @Test
    void settersAndGetters() {
        var conf = new EmailConfirmationControllerConfigurationProperties();
        conf.setPath("/foo");
        conf.setFailureRedirect("/bar");
        conf.setSuccessfulRedirect("/xxx");
        assertThat(conf.getSuccessfulRedirect()).isEqualTo("/xxx");
        assertThat(conf.getFailureRedirect()).isEqualTo("/bar");
        assertThat(conf.getPath()).isEqualTo("/foo");
    }

}