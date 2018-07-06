package pojo.garmin.sleep;

public class Awake {
	
	private Integer startTimeInSeconds;
    private Integer endTimeInSeconds;
    
    public int getEndTimeInSeconds() {return endTimeInSeconds;}
    public int getStartTimeInSeconds() {return startTimeInSeconds;}

    public void setStartTimeInSeconds(int startTimeInSeconds) {this.startTimeInSeconds = startTimeInSeconds;}
    public void setEndTimeInSeconds(int endTimeInSeconds) {this.endTimeInSeconds = endTimeInSeconds;}
}
