package org.projectcheckins.core;

import static java.lang.String.format;
import static java.util.TimeZone.getDefault;
import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.views.fields.elements.Option;
import java.util.TimeZone;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.TimeZoneFetcher;

@MicronautTest
class TimeZoneFetcherTest {
  @Test
  void testGenerate(TimeZoneFetcher timeZoneFetcher) {
    final TimeZone tz = getDefault();
    final String expectedValue = tz.getID();
    final String expectedMessage = format("%s (%s)", expectedValue, tz.getDisplayName());
    assertThat(timeZoneFetcher.generate(TimeZone.class))
        .anySatisfy(x -> assertThat(x)
            .hasFieldOrPropertyWithValue("value", expectedValue)
            .extracting(Option::label)
            .hasFieldOrPropertyWithValue("defaultMessage", expectedMessage));
  }
}
