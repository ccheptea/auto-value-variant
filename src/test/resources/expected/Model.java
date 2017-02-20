package input;

import java.lang.Override;
import java.lang.String;

final class AutoValue_Model extends $AutoValue_Model {
  AutoValue_Model(String a, String b, String c) {
    super(a, b, c);
  }

  @Override
  public final boolean variantOf(Model other) {
    return variantOf(other, "");
  }

  @Override
  public final boolean variantOf(Model other, String group) {
    return group != null && other != null && groupFieldsEqual(other, group) && !equals(other);
  }

  @Override
  public final boolean variantOrEqual(Model other) {
    return other != null && (other == this || groupFieldsEqual(other, ""));
  }

  @Override
  public final boolean variantOrEqual(Model other, String group) {
    return group != null && other != null && (other == this || groupFieldsEqual(other, group));
  }

  private boolean groupFieldsEqual(Model other, String group) {
    if(group.equals("")){
      return (this.a().equals(other.a()))
              && (this.b().equals(other.b()));
    }else{
      throw new RuntimeException("no properties for group: " + group);
    }
  }
}