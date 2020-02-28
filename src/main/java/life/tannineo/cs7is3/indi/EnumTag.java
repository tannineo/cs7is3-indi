package life.tannineo.cs7is3.indi;

public enum EnumTag {

  ID(".I", "id"), TITLE(".T", "title"), AUTHORS(".A", "authors"), BIBLIOGRAPHY(".B", "bibliography"),
  WORDS(".W", "words");

  // the tag string
  private String tag;

  // field name of the fieldType
  private String fieldName;

  public static String[] getAllFields() {
    return new String[] { "id", "title", "authors", "bibliography", "words" };
  }

  private EnumTag(String tag, String fieldName) {
    this.tag = tag;
    this.fieldName = fieldName;
  }

  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }
}
