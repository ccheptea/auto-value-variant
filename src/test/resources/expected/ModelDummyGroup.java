package input;

import java.lang.Override;
import java.lang.String;

final class AutoValue_ModelDummyGroup extends $AutoValue_ModelDummyGroup {
    AutoValue_ModelDummyGroup(String a, String b, String c, boolean d) {
        super(a, b, c, d);
    }

    @Override
    public final boolean variantOf(ModelDummyGroup other) {
        return variantOf(other, "");
    }

    @Override
    public final boolean variantOf(ModelDummyGroup other, String group) {
        return group != null && other != null && groupFieldsEqual(other, group) && !equals(other);
    }

    @Override
    public final boolean variantOrEqual(ModelDummyGroup other) {
        return other != null && (other == this || groupFieldsEqual(other, ""));
    }

    @Override
    public final boolean variantOrEqual(ModelDummyGroup other, String group) {
        return group != null && other != null && (other == this || groupFieldsEqual(other, group));
    }

    private boolean groupFieldsEqual(ModelDummyGroup other, String group) {
        if (group.equals("Dummy")) {
            return (this.a().equals(other.a()))
                    && (this.b().equals(other.b()));
        } else {
            throw new RuntimeException("no properties for group: " + group);
        }
    }
}