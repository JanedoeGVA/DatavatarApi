package pojo.garmin.sleep;

public class Sleep {
	
	private String summaryId;
	private String calendarDate;
	private int durationInSeconds;
	private int startTimeInSeconds;
	private int startTimeOffsetInSeconds;
	private int unmeasurableSleepInSeconds;
	private int deepSleepDurationInSeconds;
	private int lightSleepDurationInSeconds;
	private int remSleepInSeconds;
	private int awakeDurationInSeconds;
	private SleepLevelsMap sleepLevelsMap;
	private String validation;

	public String getSummaryId() {return summaryId;}
	public String getCalendarDate() {return calendarDate;}
	public int getDurationInSeconds() {return durationInSeconds;}
	public int getStartTimeInSeconds() {return startTimeInSeconds;}
	public int getStartTimeOffsetInSeconds() {return startTimeOffsetInSeconds;}
	public int getUnmeasurableSleepInSeconds() {return unmeasurableSleepInSeconds;}
	public int getDeepSleepDurationInSeconds() {return deepSleepDurationInSeconds;}
	public int getLightSleepDurationInSeconds() {return lightSleepDurationInSeconds;}
	public int getRemSleepInSeconds() {return remSleepInSeconds;}
	public int getAwakeDurationInSeconds() {return awakeDurationInSeconds;}
	public SleepLevelsMap getSleepLevelsMap() {return sleepLevelsMap;}
	public String getValidation() {return validation;}
	
	public void setSummaryId(String summaryId) {this.summaryId = summaryId;}
	public void setCalendarDate(String calendarDate) {this.calendarDate = calendarDate;}
	public void setDurationInSeconds(int durationInSeconds) {this.durationInSeconds = durationInSeconds;}
	public void setStartTimeInSeconds(int startTimeInSeconds) {this.startTimeInSeconds = startTimeInSeconds;}
	public void setStartTimeOffsetInSeconds(int startTimeOffsetInSeconds) {this.startTimeOffsetInSeconds = startTimeOffsetInSeconds;}
	public void setUnmeasurableSleepInSeconds(int unmeasurableSleepInSeconds) {this.unmeasurableSleepInSeconds = unmeasurableSleepInSeconds;}
	public void setDeepSleepDurationInSeconds(int deepSleepDurationInSeconds) {this.deepSleepDurationInSeconds = deepSleepDurationInSeconds;}
	public void setLightSleepDurationInSeconds(int lightSleepDurationInSeconds) {this.lightSleepDurationInSeconds = lightSleepDurationInSeconds;}
	public void setRemSleepInSeconds(int remSleepInSeconds) {this.remSleepInSeconds = remSleepInSeconds;}
	public void setAwakeDurationInSeconds(int awakeDurationInSeconds) {this.awakeDurationInSeconds = awakeDurationInSeconds;}
	public void setSleepLevelsMap(SleepLevelsMap sleepLevelsMap) {this.sleepLevelsMap = sleepLevelsMap;}
	public void setValidation(String validation) {this.validation = validation;}

	
}
	