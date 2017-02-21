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

import java.util.*;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;

@AutoService(AutoValueExtension.class)
public class AutoValueVariantExtension extends AutoValueExtension {
    private final String variantInterface = Variant.class.getCanonicalName();

    @Override
    public boolean applicable(Context context) {
        return getInterfaceType(variantInterface, context) != null;
    }

    @Override
    public boolean mustBeFinal(Context context) {
        return false;
    }

    @Override
    public String generateClass(Context context, String className, String classToExtend, boolean isFinal) {
        String packageName = context.packageName();
        String superQualifiedName = context.autoValueClass().getQualifiedName().toString();
        Map<String, ExecutableElement> properties = context.properties();
        TypeName parametrizedType = getParametrizedType(context);

        if (parametrizedType == null) {
            throw new RuntimeException("Missing Parametrized Type " + superQualifiedName);
        } else if (!superQualifiedName.equals(parametrizedType.toString())) {
            throw new RuntimeException("Parametrized Type must be same as AutoValue class: "
                    + superQualifiedName
                    + ". Found " + parametrizedType + " instead.");
        }

        TypeSpec subclass = TypeSpec.classBuilder(className)
                .addModifiers(isFinal ? Modifier.FINAL : Modifier.ABSTRACT)
                .superclass(ClassName.get(packageName, classToExtend))
                .addMethod(generateConstructor(properties))
                .addMethod(generateVariantOf(parametrizedType))
                .addMethod(generateVariantOfInGroup(parametrizedType))
                .addMethod(generateVariantOrEqual(parametrizedType))
                .addMethod(generateVariantOrEqualInGroup(parametrizedType))
                .addMethod(generateGroupFieldsEqual(parametrizedType, properties))
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, subclass).build();
        return javaFile.toString();
    }

    private TypeName getParametrizedType(Context context) {
        TypeMirror interfaceType = getInterfaceType(variantInterface, context);

        if (interfaceType == null) {
            return null;
        }
        try {
            ParameterizedTypeName parameterizedTypeName = ((ParameterizedTypeName) ParameterizedTypeName.get(interfaceType));
            return parameterizedTypeName.typeArguments.get(0);
        } catch (Exception e) {
            return null;
        }
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

    private static MethodSpec generateGroupFieldsEqual(TypeName parameterName, Map<String, ExecutableElement> properties) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("groupFieldsEqual")
                .addModifiers(Modifier.PRIVATE) //
                .returns(TypeName.BOOLEAN)
                .addParameter(parameterName, "other")
                .addParameter(String.class, "group");

        Map<String, List<ExecutableElement>> groups = getGroupedProperties(properties);

        if (groups.size() == 0) {
            builder.addCode("return true;\n");
            return builder.build();
        }

        boolean firstIfBranch = true;
        boolean firstProperty;
        for (Map.Entry<String, List<ExecutableElement>> group : groups.entrySet()) {
            builder.addCode(firstIfBranch ? "if(" : "else if(");
            builder.addCode("group.equals(\"$L\")){\n", group.getKey());
            builder.addCode("   return ");

            firstProperty = true;
            for (ExecutableElement property : group.getValue()) {
                if (!firstProperty) {
                    builder.addCode("\n      && ");
                }
                builder.addCode(generateEqualsExpression(property));
                firstProperty = false;
            }

            builder.addCode(";\n");
            builder.addCode("}");

            firstIfBranch = false;
        }

        builder.addCode("else{\n   throw new RuntimeException(\"no properties for group: \" + group);\n}\n");

        return builder.build();
    }

    private static MethodSpec generateVariantOf(TypeName parameterName) {
        return MethodSpec.methodBuilder("variantOf") //
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL) //
                .returns(TypeName.BOOLEAN)
                .addParameter(parameterName, "other")
                .addCode("return variantOf(other, \"\");\n")
                .build();
    }

    private static MethodSpec generateVariantOfInGroup(TypeName parameterName) {
        return MethodSpec.methodBuilder("variantOf") //
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL) //
                .returns(TypeName.BOOLEAN)
                .addParameter(parameterName, "other")
                .addParameter(String.class, "group")
                .addCode("return group != null && other != null && groupFieldsEqual(other, group) && !equals(other);\n")
                .build();
    }

    private static MethodSpec generateVariantOrEqual(TypeName parameterName) {
        return MethodSpec.methodBuilder("variantOrEqual") //
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL) //
                .returns(TypeName.BOOLEAN)
                .addParameter(parameterName, "other")
                .addCode("return other != null && (other == this || groupFieldsEqual(other, \"\"));\n")
                .build();
    }

    private static MethodSpec generateVariantOrEqualInGroup(TypeName parameterName) {
        return MethodSpec.methodBuilder("variantOrEqual") //
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL) //
                .returns(TypeName.BOOLEAN)
                .addParameter(parameterName, "other")
                .addParameter(String.class, "group")
                .addCode("return group != null && other != null && (other == this || groupFieldsEqual(other, group));\n")
                .build();
    }

    private static Map<String, List<ExecutableElement>> getGroupedProperties(Map<String, ExecutableElement> propertyElements) {
        Map<String, List<ExecutableElement>> groups = new HashMap<>();
        for (ExecutableElement propertyElement : propertyElements.values()) {
            NonVariant nonVariant = propertyElement.getAnnotation(NonVariant.class);
            if (nonVariant != null) {
                for (String groupName : nonVariant.value()) {
                    if (!groups.containsKey(groupName)) {
                        groups.put(groupName, new ArrayList<ExecutableElement>());
                    }
                    groups.get(groupName).add(propertyElement);
                }
            }
        }
        return groups;
    }

    private static CodeBlock generateEqualsExpression(ExecutableElement propertyElement) {
        String methodName = propertyElement.getSimpleName().toString();
        TypeName propertyType = TypeName.get(propertyElement.getReturnType());
        Set<String> propertyAnnotations = getAnnotations(propertyElement);

        boolean nullable = propertyAnnotations.contains("Nullable");

        if (propertyType.equals(TypeName.FLOAT)) {
            return CodeBlock.of("(Float.floatToIntBits(this.$N()) == Float.floatToIntBits(other.$N()))",
                    methodName, methodName);
        } else if (propertyType.equals(TypeName.DOUBLE)) {
            return CodeBlock.of(
                    "(Double.doubleToLongBits(this.$N()) == Double.doubleToLongBits(other.$N()))", methodName,
                    methodName);
        } else if (propertyType.isPrimitive()) {
            return CodeBlock.of("(this.$N() == other.$N())", methodName, methodName);
        } else if (propertyType instanceof ArrayTypeName) {
            return CodeBlock.of("($T.equals(this.$N(), other.$N()))", Arrays.class, methodName,
                    methodName);
        } else {
            if (nullable) {
                return CodeBlock.of(
                        "((this.$N() == null) ? (other.$N() == null) : this.$N().equals(other.$N()))", methodName,
                        methodName, methodName, methodName);
            } else {
                return CodeBlock.of("(this.$N().equals(other.$N()))", methodName, methodName);
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
        for (TypeMirror anInterface : interfaces) {
            if (anInterface.toString().startsWith(type)) {
                return anInterface;
            }
        }
        return null;
    }
}
