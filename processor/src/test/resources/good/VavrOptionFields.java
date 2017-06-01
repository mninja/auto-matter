package foo;

import io.vavr.control.Option;

import javax.annotation.Nullable;

import io.norberg.automatter.AutoMatter;

@AutoMatter
public interface VavrOptionFields {
  Option<String> foo();
  @Nullable Option<String> bar();
}