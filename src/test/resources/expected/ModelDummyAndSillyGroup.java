package input;

import java.lang.Override;
import java.lang.String;

final class AutoValue_ModelDummyAndSillyGroup extends $AutoValue_ModelDummyAndSillyGroup {
    AutoValue_ModelDummyAndSillyGroup(String a, String b, String c, boolean d) {
        super(a, b, c, d);
    }

    @Override
    public final boolean variantOf(ModelDummyAndSillyGroup other) {
        return variantOf(other, "");
    }

    @Override
    public final boolean variantOf(ModelDummyAndSillyGroup other, String group) {
        return group != null && other != null && groupFieldsEqual(other, group) && !equals(other);
    }

    @Override
    public final boolean variantOrEqual(ModelDummyAndSillyGroup other) {
        return other != null && (other == this || groupFieldsEqual(other, ""));
    }

    @Override
    public final boolean variantOrEqual(ModelDummyAndSillyGroup other, String group) {
        return group != null && other != null && (other == this || groupFieldsEqual(other, group));
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