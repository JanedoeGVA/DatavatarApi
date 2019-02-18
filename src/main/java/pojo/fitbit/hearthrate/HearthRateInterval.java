
package pojo.fitbit.hearthrate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HearthRateInterval {

    private List<ActivitiesHeart> activitiesHeart = null;
    private ActivitiesHeartIntraday activitiesHeartIntraday;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public List<ActivitiesHeart> getActivitiesHeart() {
        return activitiesHeart;
    }

    public void setActivitiesHeart(List<ActivitiesHeart> activitiesHeart) {
        this.activitiesHeart = activitiesHeart;
    }

    public ActivitiesHeartIntraday getActivitiesHeartIntraday() {
        return activitiesHeartIntraday;
    }

    public void setActivitiesHeartIntraday(ActivitiesHeartIntraday activitiesHeartIntraday) {
        this.activitiesHeartIntraday = activitiesHeartIntraday;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
