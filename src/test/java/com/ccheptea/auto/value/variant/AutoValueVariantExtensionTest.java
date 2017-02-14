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

import com.google.auto.value.processor.AutoValueProcessor;
import com.google.testing.compile.JavaFileObjects;

import java.util.Arrays;
import javax.tools.JavaFileObject;

import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;

public class AutoValueVariantExtensionTest {
    private JavaFileObject constant;
    private JavaFileObject nullable;

    @Before
    public void setUp() {
        constant = JavaFileObjects.forSourceString("test.Constant", ""
                + "package test;\n"
                + "import java.lang.annotation.Retention;\n"
                + "import java.lang.annotation.Target;\n"
                + "import static java.lang.annotation.ElementType.FIELD;\n"
                + "import static java.lang.annotation.ElementType.METHOD;\n"
                + "import static java.lang.annotation.ElementType.PARAMETER;\n"
                + "import static java.lang.annotation.RetentionPolicy.SOURCE;\n"
                + "@Retention(SOURCE)\n"
                + "@Target({METHOD, PARAMETER, FIELD})\n"
                + "public @interface Constant {\n"
                + "}");

        nullable = JavaFileObjects.forSourceString("test.Nullable", ""
                + "package test;\n"
                + "import java.lang.annotation.Retention;\n"
                + "import java.lang.annotation.Target;\n"
                + "import static java.lang.annotation.ElementType.FIELD;\n"
                + "import static java.lang.annotation.ElementType.METHOD;\n"
                + "import static java.lang.annotation.ElementType.PARAMETER;\n"
                + "import static java.lang.annotation.RetentionPolicy.CLASS;\n"
                + "@Retention(CLASS)\n"
                + "@Target({METHOD, PARAMETER, FIELD})\n"
                + "public @interface Nullable {\n"
                + "}");
    }

    @Test
    public void simple() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.Test", ""
                + "package test;\n"
                + "import com.google.auto.value.AutoValue;\n"
                + "import com.ccheptea.auto.value.variant.Variant;\n"
                + "@AutoValue public abstract class Test implements Variant<Test> {\n"
                + "@Constant public abstract String a();\n"
                + "public abstract int b();\n"
                + "public abstract long c();\n"
                + "public abstract float d();\n"
                + "public abstract double e();\n"
                + "public abstract boolean f();\n"
                + "public abstract int[] g();\n"
                + "public abstract String h();\n"
                + "@Nullable public abstract String i();\n"
                + "}\n"
        );

        JavaFileObject expectedSource = JavaFileObjects.forSourceString("test/AutoValue_Test", ""
                + "package test;\n"
                + "\n"
                + "import java.lang.Override;\n"
                + "import java.lang.String;\n"
                + "\n"
                + "final class AutoValue_Test extends $AutoValue_Test {\n"
                + "  AutoValue_Test(String a, int b, long c, float d, double e, boolean f, int[] g, String h, String i) {\n"
                + "    super(a, b, c, d, e, f, g, h, i);\n"
                + "  }\n"
                + "\n"
                + "  @Override\n"
                + "  public final boolean variantOf(Test that) {\n"
                + "    if (this == that) return false;\n"
                + "    if(!(true\n"
                + "          && (this.a().equals(that.a())))){\n"
                + "      return false;\n"
                + "    }\n"
                + "    return !this.equals(that);"
                + "  }\n"
                + "}"
        );

        assertAbout(javaSources())
                .that(Arrays.asList(constant, nullable, source))
                .processedWith(new AutoValueProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedSource);
    }
}