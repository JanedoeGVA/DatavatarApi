package pojo;


import javax.xml.bind.annotation.XmlElement;

public class HeartRate {

    @XmlElement(name = "heart-rate")
    private int heartRate;
    private int date;


    public HeartRate(int heartRate, int date) {
        this.heartRate = heartRate;
        this.date = date;
    }

    public HeartRate() {

    }

    public int getDate() {return date; }
    public int getHeartRate() {return heartRate;}

    public void setDate(int date) {this.date = date;}
    public void setHeartRate(int heartRate) {this.heartRate = heartRate; }
}
