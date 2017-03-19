package com.ccheptea.auto.value.variant;

import com.ccheptea.auto.value.variant.runtime.NonVariant;
import com.ccheptea.auto.value.variant.runtime.Variant;
import com.google.auto.value.AutoValue;

/**
 * Created by constantin.cheptea
 * on 24/02/2017.
 */
@AutoValue
public abstract class CarSingleGroup implements Variant<CarSingleGroup> {
    @NonVariant("identity")
    public abstract String manufacturer();

    @NonVariant("identity")
    public abstract String model();

    public abstract String plateNumber();

    public abstract int color();

    public abstract String body();

    static class VariantGroups {
        static final String IDENTITY = "identity";
    }
}
