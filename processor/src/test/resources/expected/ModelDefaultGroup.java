package input;

import java.lang.Override;
import java.lang.String;

final class AutoValue_ModelDefaultGroup extends $AutoValue_ModelDefaultGroup {
    AutoValue_ModelDefaultGroup(String a, String b, String c, boolean d) {
        super(a, b, c, d);
    }

    @Override
    public final boolean like(ModelDefaultGroup other) {
        return like(other, "");
    }

    @Override
    public final boolean like(ModelDefaultGroup other, String group) {
        return group != null
                && other != null
                && other instanceof ModelDefaultGroup
                && groupFieldsEqual((ModelDefaultGroup)other, group)
                && !equals(other);
    }

    @Override
    public final boolean likeOrEqual(ModelDefaultGroup other) {
        return likeOrEqual(other, "");
    }

    @Override
    public final boolean likeOrEqual(ModelDefaultGroup other, String group) {
        return group != null
                && other != null
                && other instanceof ModelDefaultGroup
                && (other == this || groupFieldsEqual((ModelDefaultGroup) other, group));
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