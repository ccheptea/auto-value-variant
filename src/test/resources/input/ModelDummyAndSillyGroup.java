package input;

import com.ccheptea.auto.value.variant.Variant;
import com.ccheptea.auto.value.variant.NonVariant;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ModelDummyAndSillyGroup implements Variant<ModelDummyAndSillyGroup> {
    @NonVariant("Dummy")
    public abstract String a();

    @NonVariant("Dummy")
    public abstract String b();

    @NonVariant("Silly")
    public abstract String c();

    public abstract boolean d();
}
