package io.norberg.automatter.processor;

import com.google.common.collect.Lists;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import io.vavr.collection.Array;

import javax.lang.model.element.ExecutableElement;

import java.util.List;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;

public class VavrCollectionProcessor implements FieldProcessor {
  public VavrCollectionProcessor(AutoMatterProcessor processor) {

  }

  @Override
  public FieldSpec builderField(Descriptor d, ExecutableElement field) throws AutoMatterProcessorException {
    return FieldSpec.builder(mutableTypeFor(d, field), fieldName(field), PRIVATE).build();
  }

  @Override
  public FieldSpec valueField(Descriptor d, ExecutableElement field) throws AutoMatterProcessorException {
    return FieldSpec.builder(fieldType(d, field), fieldName(field), PRIVATE, FINAL).build();
  }

  @Override
  public Iterable<MethodSpec> accessors(Descriptor d, ExecutableElement field) throws AutoMatterProcessorException {
    List<MethodSpec> result = Lists.newArrayList();

    result.add(getter(d, field));
    if (!isMap(field)) {
      result.add(collectionSetter(d, field));
      result.add(collectionCollectionSetter(d, field));
      result.add(collectionIterableSetter(d, field));
      result.add(collectionIteratorSetter(d, field));
      result.add(collectionVarargSetter(d, field));

      MethodSpec adder = collectionAdder(d, field);
      if (adder != null) {
        result.add(adder);
      }
    } else {
      result.add(mapSetter(d, field));
      for (int i = 1; i <= 5; i++) {
        result.add(mapSetterPairs(d, field, i));
      }

      MethodSpec putter = mapPutter(d, field);
      if (putter != null) {
        result.add(putter);
      }
    }
    return result;
  }

  @Override
  public Iterable<Statement> defaultConstructor(Descriptor d, ExecutableElement field) {
    return null;
  }

  @Override
  public Iterable<Statement> copyValueConstructor(Descriptor d, ExecutableElement field) throws AutoMatterProcessorException {
    return null;
  }

  @Override
  public Iterable<Statement> copyBuilderConstructor(Descriptor d, ExecutableElement field) throws AutoMatterProcessorException {
    return null;
  }

  @Override
  public BuildStatements build(Descriptor d, ExecutableElement field) throws AutoMatterProcessorException {
    return null;
  }

  @Override
  public Iterable<Statement> valueConstructor(Descriptor d, ExecutableElement field) throws AutoMatterProcessorException {
    return null;
  }

  private TypeName mutableTypeFor(Descriptor d, ExecutableElement field) {
    return null;
  }

  private TypeName collectionImplType(ExecutableElement field) {
    return null;
  }

  private MethodSpec getter(final Descriptor d, final ExecutableElement field) throws AutoMatterProcessorException {
    String fieldName = fieldName(field);

    MethodSpec.Builder getter = MethodSpec.methodBuilder(fieldName)
      .addModifiers(PUBLIC)
      .returns(fieldType(d, field));

    if (shouldEnforceNonNull(field)) {
      getter.beginControlFlow("if (this.$N == null)", fieldName)
        .addStatement("this.$N = $T.empty()", fieldName, collectionImplType(field))
        .endControlFlow();
    }
    getter.addStatement("return $N", fieldName);

    return getter.build();
  }

  private boolean isMap(final ExecutableElement field) {
    final String returnType = field.getReturnType().toString();
    return returnType.startsWith("io.vavr.collection.Map<");
  }
}
