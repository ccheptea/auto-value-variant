package com.ccheptea.auto.value.variant;

import com.google.auto.value.AutoValue;

/**
 * Created by constantin.cheptea
 * on 24/02/2017.
 */
@AutoValue
public abstract class CarMultipleGroups implements Variant<CarMultipleGroups> {

    @NonVariant(VariantGroups.IDENTITY)
    public abstract String manufacturer();

    @NonVariant({VariantGroups.IDENTITY, VariantGroups.MODEL_AND_BODY})
    public abstract String model();

    public abstract String plateNumber();

    @NonVariant(VariantGroups.ASPECT)
    public abstract int color();

    @NonVariant({VariantGroups.ASPECT, VariantGroups.MODEL_AND_BODY})
    public abstract String body();

    public static class VariantGroups {
        public static final String IDENTITY = "identity";
        public static final String ASPECT = "aspect";
        public static final String MODEL_AND_BODY = "model_and_body";
    }
}