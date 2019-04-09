package pojo;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

public class HeartRateData {

    @XmlElement(name = "lstHeartRate")
    private List<HeartRate> lstHeartRate;



    public HeartRateData() {
        this.lstHeartRate = new ArrayList<HeartRate>();
    }

    public void addHeartRateData (HeartRate heartRate) {
        this.lstHeartRate.add(heartRate);
    }

    public List<HeartRate> getLstHeartRate() {
        return lstHeartRate;
    }

    public void setLstHeartRate(List<HeartRate> lstHeartRate) {
        this.lstHeartRate = lstHeartRate;
    }
}
