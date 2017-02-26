package com.ccheptea.auto.value.variant;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by constantin.cheptea
 * on 26/02/2017.
 */
@Retention(SOURCE)
@Target({METHOD, PARAMETER, FIELD})
public @interface Nullable {
}
