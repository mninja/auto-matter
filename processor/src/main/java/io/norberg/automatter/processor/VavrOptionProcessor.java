package io.norberg.automatter.processor;

import com.google.common.collect.Lists;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import io.vavr.control.Option;

import javax.lang.model.element.ExecutableElement;

import java.util.Collections;
import java.util.List;

import static com.squareup.javapoet.WildcardTypeName.subtypeOf;
import static io.norberg.automatter.processor.AutoMatterProcessor.builderType;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;

public class VavrOptionProcessor extends DefaultProcessor {
  @Override
  public FieldSpec builderField(Descriptor d, ExecutableElement field) throws AutoMatterProcessorException {
    return FieldSpec.builder(fieldType(d, field), fieldName(field), PRIVATE).build();
  }

  @Override
  public FieldSpec valueField(Descriptor d, ExecutableElement field) throws AutoMatterProcessorException {
    return FieldSpec.builder(fieldType(d, field), fieldName(field), PRIVATE, FINAL).build();
  }

  @Override
  public Iterable<MethodSpec> accessors(Descriptor d, ExecutableElement field) throws AutoMatterProcessorException {
    List<MethodSpec> methods = Lists.newArrayList();
    methods.add(getter(d, field));
    methods.add(optionRawSetter(d, field));
    methods.add(optionSetter(d, field));
    return methods;
  }

  @Override
  public Iterable<Statement> defaultConstructor(Descriptor d, ExecutableElement field) {
     if (shouldEnforceNonNull(field)) {
      ClassName type = ClassName.get(Option.class);
      return Collections.singletonList(
          new Statement("this.$N = $T.$L()", fieldName(field), type, "none"));
    }
    return Collections.emptyList();
  }

  private MethodSpec optionRawSetter(final Descriptor d, final ExecutableElement field) {
    String fieldName = fieldName(field);
    ClassName type = ClassName.get(Option.class);
    TypeName valueType = genericArgument(field, 0);

    return MethodSpec.methodBuilder(fieldName)
        .addModifiers(PUBLIC)
        .addParameter(valueType, fieldName)
        .returns(builderType(d))
        .addStatement("return $N($T.$N($N))", fieldName, type, "of", fieldName)
        .build();
  }

  private MethodSpec optionSetter(final Descriptor d, final ExecutableElement field)
      throws AutoMatterProcessorException {
    String fieldName = fieldName(field);
    TypeName valueType = genericArgument(field, 0);
    ClassName optionalType = ClassName.get(Option.class);
    TypeName parameterType = ParameterizedTypeName.get(optionalType, subtypeOf(valueType));

    AnnotationSpec suppressUncheckedAnnotation = AnnotationSpec.builder(SuppressWarnings.class)
        .addMember("value", "$S", "unchecked")
        .build();

    MethodSpec.Builder setter = MethodSpec.methodBuilder(fieldName)
        .addAnnotation(suppressUncheckedAnnotation)
        .addModifiers(PUBLIC)
        .addParameter(parameterType, fieldName)
        .returns(builderType(d));

    if (shouldEnforceNonNull(field)) {
      assertNotNull(setter, fieldName);
    }

    setter.addStatement("this.$N = ($T)$N", fieldName, fieldType(d, field), fieldName);

    return setter.addStatement("return this").build();
  }
}
