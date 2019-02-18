
package pojo.fitbit.hearthrate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivitiesHeartIntraday {

    private List<Dataset> dataset = null;
    private int datasetInterval;
    private String datasetType;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public List<Dataset> getDataset() {
        return dataset;
    }

    public void setDataset(List<Dataset> dataset) {
        this.dataset = dataset;
    }

    public int getDatasetInterval() {
        return datasetInterval;
    }

    public void setDatasetInterval(int datasetInterval) {
        this.datasetInterval = datasetInterval;
    }

    public String getDatasetType() {
        return datasetType;
    }

    public void setDatasetType(String datasetType) {
        this.datasetType = datasetType;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
