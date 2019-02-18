
package pojo.fitbit.hearthrate;

import java.util.HashMap;
import java.util.Map;

public class Dataset {

    private String time;
    private int value;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
