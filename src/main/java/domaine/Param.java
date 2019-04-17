package domaine;

public class Param {

    public enum TypeParam {QUERY_PARAM,HEADER_PARAM}

    private String key;
    private String value;
    private TypeParam type;

    public Param(String key, String value,TypeParam type) {
        this.key = key;
        this.value = value;
        this.type=type;
    }

    public TypeParam getType() {
        return type;
    }

    public void setType(TypeParam type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }
    public String getValue() {
        return value;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public void setValue(String value) {
        this.value = value;
    }

}
