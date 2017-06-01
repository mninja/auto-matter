package io.norberg.automatter;

import com.google.common.collect.ImmutableList;
import io.vavr.control.Option;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.annotation.Nullable;
import java.util.List;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class VavrOptionTest {

  public @Rule ExpectedException expectedException = ExpectedException.none();

  @AutoMatter
  interface Options {

    Option<String> foo();
    Option<List<String>> bar();

    @Nullable
    Option<String> baz();
  }

  OptionsBuilder builder;

  @Before
  public void setup() {
    builder = new OptionsBuilder();
  }

  @Test
  public void testDefaults() {
    final Options foobar = builder.build();
    assertThat(foobar.foo(), is(Option.<String>none()));
    assertThat(foobar.bar(), is(Option.<List<String>>none()));
    assertThat(foobar.baz(), is(nullValue()));
  }

  @Test
  public void verifySettingNullGivesAbsent() {
    builder.foo((String) null);
    final Options foobar = builder.build();
    assertThat(foobar.foo(), is(Option.<String>none()));
  }

  @Test
  public void verifySettingValueGivesPresent() {
    builder.foo("bar");
    final Options foobar = builder.build();
    assertThat(foobar.foo(), is(Option.some("bar")));
  }

  @Test
  public void verifySettingNonNullableNullNPEs() {
    expectedException.expect(NullPointerException.class);
    expectedException.expectMessage("foo");
    builder.foo((Option<String>) null);
  }

  @Test
  public void verifySettingNullableNull() {
    builder.baz("hello");
    builder.baz((Option<String>) null);
    final Options foobar = builder.build();
    assertThat(foobar.baz(), is(nullValue()));
  }

  @Test
  public void verifySettingExtendingValue() {
    builder.bar(Option.of(ImmutableList.of("foo", "bar")));
  }
}
