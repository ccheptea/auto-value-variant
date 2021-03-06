package input;

import java.lang.Object;
import java.lang.Override;
import java.lang.String;

final class AutoValue_ModelDummyGroup extends $AutoValue_ModelDummyGroup {
    AutoValue_ModelDummyGroup(String a, String b, String c, boolean d) {
        super(a, b, c, d);
    }

    @Override
    public final boolean like(Object other) {
        return like(other, "");
    }

    @Override
    public final boolean like(Object other, String group) {
        return group != null
                && other != null
                && other instanceof ModelDummyGroup
                && groupFieldsEqual((ModelDummyGroup) other, group)
                && !equals(other);
    }

    @Override
    public final boolean likeOrEqual(Object other) {
        return likeOrEqual(other, "");
    }

    @Override
    public final boolean likeOrEqual(Object other, String group) {
        return group != null
                && other != null
                && other instanceof ModelDummyGroup
                && (other == this || groupFieldsEqual((ModelDummyGroup) other, group));
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