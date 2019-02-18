
package pojo.fitbit.hearthrate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivitiesHeart {

    private List<Object> customHeartRateZones = null;
    private String dateTime;
    private List<HeartRateZone> heartRateZones = null;
    private String value;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public List<Object> getCustomHeartRateZones() {
        return customHeartRateZones;
    }

    public void setCustomHeartRateZones(List<Object> customHeartRateZones) {
        this.customHeartRateZones = customHeartRateZones;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public List<HeartRateZone> getHeartRateZones() {
        return heartRateZones;
    }

    public void setHeartRateZones(List<HeartRateZone> heartRateZones) {
        this.heartRateZones = heartRateZones;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
