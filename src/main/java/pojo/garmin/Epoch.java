package pojo.garmin;

public class Epoch {

	private String summaryId;
	private String activityType;
	private int activeKilocalories;
	private int steps;
	private double distanceInMeters;
	private int durationInSeconds;
	private int activeTimeInSeconds;
	private int startTimeInSeconds;
	private int startTimeOffsetInSeconds;
	private double met;
	private String intensity;
	private double meanMotionIntensity;
	private double maxMotionIntensity;
	
	public String getSummaryId() {
		return summaryId;
	}
	public void setSummaryId(String summaryId) {
		this.summaryId = summaryId;
	}
	public String getActivityType() {
		return activityType;
	}
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
	public int getActiveKilocalories() {
		return activeKilocalories;
	}
	public void setActiveKilocalories(int activeKilocalories) {
		this.activeKilocalories = activeKilocalories;
	}
	public int getSteps() {
		return steps;
	}
	public void setSteps(int steps) {
		this.steps = steps;
	}
	public double getDistanceInMeters() {
		return distanceInMeters;
	}
	public void setDistanceInMeters(double distanceInMeters) {
		this.distanceInMeters = distanceInMeters;
	}
	public int getDurationInSeconds() {
		return durationInSeconds;
	}
	public void setDurationInSeconds(int durationInSeconds) {
		this.durationInSeconds = durationInSeconds;
	}
	public int getActiveTimeInSeconds() {
		return activeTimeInSeconds;
	}
	public void setActiveTimeInSeconds(int activeTimeInSeconds) {
		this.activeTimeInSeconds = activeTimeInSeconds;
	}
	public int getStartTimeInSeconds() {
		return startTimeInSeconds;
	}
	public void setStartTimeInSeconds(int startTimeInSeconds) {
		this.startTimeInSeconds = startTimeInSeconds;
	}
	public int getStartTimeOffsetInSeconds() {
		return startTimeOffsetInSeconds;
	}
	public void setStartTimeOffsetInSeconds(int startTimeOffsetInSeconds) {
		this.startTimeOffsetInSeconds = startTimeOffsetInSeconds;
	}
	public double getMet() {
		return met;
	}
	public void setMet(double met) {
		this.met = met;
	}
	public String getIntensity() {
		return intensity;
	}
	public void setIntensity(String intensity) {
		this.intensity = intensity;
	}
	public double getMeanMotionIntensity() {
		return meanMotionIntensity;
	}
	public void setMeanMotionIntensity(double meanMotionIntensity) {
		this.meanMotionIntensity = meanMotionIntensity;
	}
	public double getMaxMotionIntensity() {
		return maxMotionIntensity;
	}
	public void setMaxMotionIntensity(double maxMotionIntensity) {
		this.maxMotionIntensity = maxMotionIntensity;
	}
	
	
}
