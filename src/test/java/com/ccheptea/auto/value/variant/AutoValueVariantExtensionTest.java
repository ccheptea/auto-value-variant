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

import java.io.FileNotFoundException;
import java.util.Arrays;
import javax.tools.JavaFileObject;

import org.junit.Test;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;

public class AutoValueVariantExtensionTest {

    @Test
    public void testDefaultGroup() throws FileNotFoundException {
        JavaFileObject model = JavaFileObjects.forResource("input/ModelDefaultGroup.java");
        JavaFileObject autoModel = JavaFileObjects.forResource("expected/ModelDefaultGroup.java");
        assertAbout(javaSources())
                .that(Arrays.asList(model))
                .processedWith(new AutoValueProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(autoModel);
    }

    @Test
    public void testNoGroup() throws FileNotFoundException {
        JavaFileObject model = JavaFileObjects.forResource("input/ModelNoGroup.java");
        JavaFileObject autoModel = JavaFileObjects.forResource("expected/ModelNoGroup.java");
        assertAbout(javaSources())
                .that(Arrays.asList(model))
                .processedWith(new AutoValueProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(autoModel);
    }

    @Test
    public void testDummyGroup() throws FileNotFoundException {
        JavaFileObject model = JavaFileObjects.forResource("input/ModelDummyGroup.java");
        JavaFileObject autoModel = JavaFileObjects.forResource("expected/ModelDummyGroup.java");
        assertAbout(javaSources())
                .that(Arrays.asList(model))
                .processedWith(new AutoValueProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(autoModel);
    }

    @Test
    public void testDummyAndSillyGroup() throws FileNotFoundException {
        JavaFileObject model = JavaFileObjects.forResource("input/ModelDummyAndSillyGroup.java");
        JavaFileObject autoModel = JavaFileObjects.forResource("expected/ModelDummyAndSillyGroup.java");
        assertAbout(javaSources())
                .that(Arrays.asList(model))
                .processedWith(new AutoValueProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(autoModel);
    }
}