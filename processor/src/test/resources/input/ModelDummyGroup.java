package input;

import com.ccheptea.auto.value.variant.runtime.Variant;
import com.ccheptea.auto.value.variant.runtime.NonVariant;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ModelDummyGroup implements Variant {
    @NonVariant("Dummy")
    public abstract String a();

    @NonVariant("Dummy")
    public abstract String b();

    public abstract String c();

    public abstract boolean d();
}
