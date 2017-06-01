package foo;

import io.norberg.automatter.AutoMatter;
import io.vavr.control.Option;
import javax.annotation.Generated;

@Generated("io.norberg.automatter.processor.AutoMatterProcessor")
public final class VavrOptionFieldsBuilder {

  private Option<String> foo;
  private Option<String> bar;

  public VavrOptionFieldsBuilder() {
    this.foo = Option.none();
  }

  private VavrOptionFieldsBuilder(VavrOptionFields v) {
    this.foo = v.foo();
    this.bar = v.bar();
  }

  private VavrOptionFieldsBuilder(VavrOptionFieldsBuilder v) {
    this.foo = v.foo;
    this.bar = v.bar;
  }

  public Option<String> foo() {
    return foo;
  }

  public VavrOptionFieldsBuilder foo(String foo) {
    return foo(Option.of(foo));
  }

  @SuppressWarnings("unchecked")
  public VavrOptionFieldsBuilder foo(Option<? extends String> foo) {
    if (foo == null) {
      throw new NullPointerException("foo");
    }
    this.foo = (Option<String>) foo;
    return this;
  }

  public Option<String> bar() {
    return bar;
  }

  public VavrOptionFieldsBuilder bar(String bar) {
    return bar(Option.of(bar));
  }

  @SuppressWarnings("unchecked")
  public VavrOptionFieldsBuilder bar(Option<? extends String> bar) {
    this.bar = (Option<String>) bar;
    return this;
  }

  public VavrOptionFields build() {
    return new Value(foo, bar);
  }

  public static VavrOptionFieldsBuilder from(VavrOptionFields v) {
    return new VavrOptionFieldsBuilder(v);
  }

  public static VavrOptionFieldsBuilder from(VavrOptionFieldsBuilder v) {
    return new VavrOptionFieldsBuilder(v);
  }

  private static final class Value
      implements VavrOptionFields {

    private final Option<String> foo;
    private final Option<String> bar;

    private Value(@AutoMatter.Field("foo") Option<String> foo,
                  @AutoMatter.Field("bar") Option<String> bar) {
      if (foo == null) {
        throw new NullPointerException("foo");
      }
      this.foo = foo;
      this.bar = bar;
    }

    @AutoMatter.Field
    @Override
    public Option<String> foo() {
      return foo;
    }

    @AutoMatter.Field
    @Override
    public Option<String> bar() {
      return bar;
    }

    public VavrOptionFieldsBuilder builder() {
      return new VavrOptionFieldsBuilder(this);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof VavrOptionFields)) {
        return false;
      }

      final VavrOptionFields that = (VavrOptionFields) o;

      if (foo != null ? !foo.equals(that.foo()) : that.foo() != null) {
        return false;
      }
      if (bar != null ? !bar.equals(that.bar()) : that.bar() != null) {
        return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      int result = 1;
      long temp;
      result = 31 * result + (this.foo != null ? this.foo.hashCode() : 0);
      result = 31 * result + (this.bar != null ? this.bar.hashCode() : 0);
      return result;
    }

    @Override
    public String toString() {
      return "VavrOptionFields{" +
             "foo=" + foo +
             ", bar=" + bar +
             '}';
    }
  }
}
