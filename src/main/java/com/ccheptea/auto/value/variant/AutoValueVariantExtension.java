/*
 * Copyright (C) 2016 Constantin Cheptea.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ccheptea.auto.value.variant;

import com.google.auto.service.AutoService;
import com.google.auto.value.extension.AutoValueExtension;
import com.squareup.javapoet.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.type.TypeMirror;

@AutoService(AutoValueExtension.class)
public class AutoValueVariantExtension extends AutoValueExtension {
    private final String variantInterface = Variant.class.getCanonicalName();

    @Override
    public boolean applicable(Context context) {
        return getInterfaceType(variantInterface, context) != null;
    }

    @Override
    public String generateClass(Context context, String className, String classToExtend, boolean isFinal) {
        String packageName = context.packageName();
        Name superName = context.autoValueClass().getSimpleName();
        Name superQualifiedName = context.autoValueClass().getQualifiedName();
        Map<String, ExecutableElement> properties = context.properties();
        TypeName parametrizedType = getParametrizedType(context);

        System.out.println("Auto Value Class => " + superQualifiedName);
        System.out.println("ParametrizedType => " + parametrizedType);
        if (parametrizedType == null || !superQualifiedName.toString().equals(parametrizedType.toString())) {
            throw new RuntimeException("Missing Parametrized Type " + superQualifiedName);
        }

        TypeSpec subclass = TypeSpec.classBuilder(className)
                .addModifiers(isFinal ? Modifier.FINAL : Modifier.ABSTRACT)
                .superclass(ClassName.get(packageName, classToExtend))
                .addMethod(generateConstructor(properties))
                .addMethod(generateVariantOf(superName, parametrizedType, properties))
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, subclass).build();
        return javaFile.toString();
    }

    private TypeName getParametrizedType(Context context) {
        TypeMirror interfaceType = getInterfaceType(variantInterface, context);
        if (interfaceType == null) {
            return null;
        }
        return ((ParameterizedTypeName) ParameterizedTypeName.get(interfaceType)).typeArguments.get(0);
    }

    private static MethodSpec generateConstructor(Map<String, ExecutableElement> properties) {
        List<ParameterSpec> params = new ArrayList<>();
        for (Map.Entry<String, ExecutableElement> entry : properties.entrySet()) {
            TypeName typeName = TypeName.get(entry.getValue().getReturnType());
            params.add(ParameterSpec.builder(typeName, entry.getKey()).build());
        }

        StringBuilder body = new StringBuilder("super(");
        for (int i = properties.size(); i > 0; i--) {
            body.append("$N");
            if (i > 1) body.append(", ");
        }
        body.append(")");

        return MethodSpec.constructorBuilder()
                .addParameters(params)
                .addStatement(body.toString(), properties.keySet().toArray())
                .build();
    }

    private static MethodSpec generateVariantOf(Name superName, TypeName parameterName, Map<String, ExecutableElement> properties) {

        MethodSpec.Builder builder = MethodSpec.methodBuilder("variantOf") //
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL) //
                .returns(TypeName.BOOLEAN)
                .addParameter(parameterName, "that")
                .addCode("if (this == that) return false;\n");

        List<ExecutableElement> constantProperties = new ArrayList<>();

        for (ExecutableElement property : properties.values()) {
            if (getAnnotations(property).contains("Constant")) {
                constantProperties.add(property);
            }
        }

        if (constantProperties.size() > 0) {
            builder.addCode("if(!(true");
            for (ExecutableElement property : constantProperties) {
                builder.addCode("\n      && ");
                builder.addCode(generateEqualsExpression(property));
            }
            builder
                    .addCode(")){\n")
                    .addCode("  return false;\n")
                    .addCode("}\n");
        }

        builder.addCode("return !this.equals(that);\n");

        return builder.build();
    }

    private static CodeBlock generateEqualsExpression(ExecutableElement propertyElement) {
        String methodName = propertyElement.getSimpleName().toString();
        TypeName propertyType = TypeName.get(propertyElement.getReturnType());
        Set<String> propertyAnnotations = getAnnotations(propertyElement);

        boolean nullable = propertyAnnotations.contains("Nullable");

        if (propertyType.equals(TypeName.FLOAT)) {
            return CodeBlock.of("(Float.floatToIntBits(this.$N()) == Float.floatToIntBits(that.$N()))",
                    methodName, methodName);
        } else if (propertyType.equals(TypeName.DOUBLE)) {
            return CodeBlock.of(
                    "(Double.doubleToLongBits(this.$N()) == Double.doubleToLongBits(that.$N()))", methodName,
                    methodName);
        } else if (propertyType.isPrimitive()) {
            return CodeBlock.of("(this.$N() == that.$N())", methodName, methodName);
        } else if (propertyType instanceof ArrayTypeName) {
            return CodeBlock.of("($T.equals(this.$N(), that.$N()))", Arrays.class, methodName,
                    methodName);
        } else {
            if (nullable) {
                return CodeBlock.of(
                        "((this.$N() == null) ? (that.$N() == null) : this.$N().equals(that.$N()))", methodName,
                        methodName, methodName, methodName);
            } else {
                return CodeBlock.of("(this.$N().equals(that.$N()))", methodName, methodName);
            }
        }
    }

    private static Set<String> getAnnotations(ExecutableElement element) {
        Set<String> set = new LinkedHashSet<>();

        List<? extends AnnotationMirror> annotations = element.getAnnotationMirrors();
        for (AnnotationMirror annotation : annotations) {
            set.add(annotation.getAnnotationType().asElement().getSimpleName().toString());
        }

        return Collections.unmodifiableSet(set);
    }

    private TypeMirror getInterfaceType(String type, Context context) {
        List<? extends TypeMirror> interfaces = context.autoValueClass().getInterfaces();
        System.out.println(Variant.class.getCanonicalName());
        for (TypeMirror anInterface : interfaces) {
            System.out.println(anInterface.toString());
            if (anInterface.toString().startsWith(type)) {
                return anInterface;
            }
        }
        return null;
    }
}
