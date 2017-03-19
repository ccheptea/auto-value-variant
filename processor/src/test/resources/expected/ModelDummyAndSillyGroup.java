package input;

import java.lang.Override;
import java.lang.String;

final class AutoValue_ModelDummyAndSillyGroup extends $AutoValue_ModelDummyAndSillyGroup {
    AutoValue_ModelDummyAndSillyGroup(String a, String b, String c, boolean d) {
        super(a, b, c, d);
    }

    @Override
    public final boolean like(ModelDummyAndSillyGroup other) {
        return like(other, "");
    }

    @Override
    public final boolean like(ModelDummyAndSillyGroup other, String group) {
        return group != null
                && other != null
                && other instanceof ModelDummyAndSillyGroup
                && groupFieldsEqual((ModelDummyAndSillyGroup) other, group)
                && !equals(other);
    }

    @Override
    public final boolean likeOrEqual(ModelDummyAndSillyGroup other) {
        return likeOrEqual(other, "");
    }

    @Override
    public final boolean likeOrEqual(ModelDummyAndSillyGroup other, String group) {
        return group != null
                && other != null
                && other instanceof ModelDummyAndSillyGroup
                && (other == this || groupFieldsEqual((ModelDummyAndSillyGroup) other, group));
    }

    private boolean groupFieldsEqual(ModelDummyAndSillyGroup other, String group) {
        if (group.equals("Silly")) {
            return (this.c().equals(other.c()))
        } else if (group.equals("Dummy")) {
            return (this.a().equals(other.a()))
                    && (this.b().equals(other.b()));
        } else {
            throw new RuntimeException("no properties for group: " + group);
        }
    }
}