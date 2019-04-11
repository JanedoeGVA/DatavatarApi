package pojo;


import javax.xml.bind.annotation.XmlElement;

public class HeartRate {

    @XmlElement(name = "heart-rate")
    private int heartRate;
    private long date;


    public HeartRate(int heartRate, long date) {
        this.heartRate = heartRate;
        this.date = date;
    }

    public HeartRate() {

    }

    public long getDate() {return date; }
    public int getHeartRate() {return heartRate;}

    public void setDate(long date) {this.date = date;}
    public void setHeartRate(int heartRate) {this.heartRate = heartRate; }
}
