package com.ccheptea.auto.value.variant;

import com.google.auto.value.AutoValue;

/**
 * Created by constantin.cheptea
 * on 24/02/2017.
 */
@AutoValue
public abstract class CarNoGroup implements Variant<CarNoGroup> {

    public abstract String manufacturer();

    public abstract String model();

    public abstract String plateNumber();

    public abstract int color();

    public abstract String body();
}
