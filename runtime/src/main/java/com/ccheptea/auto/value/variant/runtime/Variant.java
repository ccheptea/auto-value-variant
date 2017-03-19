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
package com.ccheptea.auto.value.variant.runtime;

public interface Variant<T> {

    /**
     * Verifies if objects are SIMILAR but NOT EQUAL in the default group "".
     *
     * @param other object to compare with <b>this</b>
     * @return true if similar but not equal; false otherwise
     */
    boolean like(T other);

    /**
     * Verifies if objects are SIMILAR but NOT EQUAL in the in the specified group.
     *
     * @param other object to compare with <b>this</b>
     * @param group in which the comparison is relevant
     * @return true if similar but not equal; false otherwise
     */
    boolean like(T other, String group);

    /**
     * Verifies if objects are SIMILAR OR EQUAL in the default group "".
     *
     * @param other object to compare with <b>this</b>
     * @return true if similar or equal; false otherwise
     */
    boolean likeOrEqual(T other);

    /**
     * Verifies if objects are SIMILAR OR EQUAL in the in the specified group.
     *
     * @param other object to compare with <b>this</b>
     * @param group in which the comparison is relevant
     * @return true if similar or equal; false otherwise
     */
    boolean likeOrEqual(T other, String group);
}
