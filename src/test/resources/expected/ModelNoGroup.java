package input;

import java.lang.Override;
import java.lang.String;

final class AutoValue_ModelNoGroup extends $AutoValue_ModelNoGroup {
    AutoValue_ModelNoGroup(String a, String b, String c, boolean d) {
        super(a, b, c, d);
    }

    @Override
    public final boolean variantOf(ModelNoGroup other) {
        return variantOf(other, "");
    }

    @Override
    public final boolean variantOf(ModelNoGroup other, String group) {
        return group != null
                && other != null
                && other instanceof ModelNoGroup
                && groupFieldsEqual((ModelNoGroup) other, group)
                && !equals(other);
    }

    @Override
    public final boolean variantOrEqual(ModelNoGroup other) {
        return variantOrEqual(other, "");
    }

    @Override
    public final boolean variantOrEqual(ModelNoGroup other, String group) {
        return group != null
                && other != null
                && other instanceof ModelNoGroup
                && (other == this || groupFieldsEqual((ModelNoGroup) other, group));
    }

    private boolean groupFieldsEqual(ModelNoGroup other, String group) {
        return true;
    }
}