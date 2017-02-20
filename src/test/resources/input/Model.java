package input;

import com.ccheptea.auto.value.variant.Variant;
import com.ccheptea.auto.value.variant.NonVariant;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Model implements Variant<Model> {
    @NonVariant
    public abstract String a();

    @NonVariant
    public abstract String b();

    public abstract String c();
    public abstract boolean d();
}
