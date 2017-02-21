package input;

import java.lang.Override;
import java.lang.String;

final class AutoValue_ModelDefaultGroup extends $AutoValue_ModelDefaultGroup {
    AutoValue_ModelDefaultGroup(String a, String b, String c, boolean d) {
        super(a, b, c, d);
    }

    @Override
    public final boolean variantOf(ModelDefaultGroup other) {
        return variantOf(other, "");
    }

    @Override
    public final boolean variantOf(ModelDefaultGroup other, String group) {
        return group != null && other != null && groupFieldsEqual(other, group) && !equals(other);
    }

    @Override
    public final boolean variantOrEqual(ModelDefaultGroup other) {
        return other != null && (other == this || groupFieldsEqual(other, ""));
    }

    @Override
    public final boolean variantOrEqual(ModelDefaultGroup other, String group) {
        return group != null && other != null && (other == this || groupFieldsEqual(other, group));
    }

    private boolean groupFieldsEqual(ModelDefaultGroup other, String group) {
        if (group.equals("")) {
            return (this.a().equals(other.a()))
                    && (this.b().equals(other.b()));
        } else {
            throw new RuntimeException("no properties for group: " + group);
        }
    }
}