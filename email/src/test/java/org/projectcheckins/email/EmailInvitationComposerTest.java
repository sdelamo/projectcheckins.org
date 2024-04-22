package org.projectcheckins.email;

import io.micronaut.context.annotation.Property;
import io.micronaut.email.BodyType;
import io.micronaut.email.Contact;
import io.micronaut.email.Email;
import io.micronaut.email.MultipartBody;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@Property(name = "email.sender", value = "info@projectcheckins.org")
@MicronautTest
class EmailInvitationComposerTest {

    @Test
    void emailInvitation(EmailInvitationComposer emailInvitationComposer) {
        Email.Builder emailBuilder = emailInvitationComposer.composeEmailInvitation(Locale.ENGLISH,
                new EmailInvitation("sergio.delamo@softamo.com", "https://projectcheckins.example.com/security/signUp"));
        assertThat(emailBuilder)
            .isNotNull()
            .extracting(Email.Builder::build)
            .hasFieldOrPropertyWithValue("subject", "You have been invited to join a Project Check-ins team")
            .hasFieldOrPropertyWithValue("from", new Contact("info@projectcheckins.org"))
            .hasFieldOrPropertyWithValue("to", List.of(new Contact("sergio.delamo@softamo.com")))
            .extracting(Email::getBody)
            .isInstanceOf(MultipartBody.class)
            .satisfies(b -> assertThat(b.get(BodyType.TEXT)).hasValueSatisfying(x -> assertThat(x).startsWith("""
                Please, click the link below and signup:
                https://projectcheckins.example.com/security/signUp""")))
            .satisfies(b -> assertThat(b.get(BodyType.HTML)).hasValueSatisfying(x -> assertThat(x).startsWith("""
                Please, click the link below and signup:
                <a href="https://projectcheckins.example.com/security/signUp""")));
    }
}
