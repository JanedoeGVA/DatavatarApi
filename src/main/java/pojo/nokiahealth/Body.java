package pojo.nokiahealth;

import java.util.List;

public class Body {
	
	private int updatetime;
	private String timezone;
	private List<Measuregrp> measuregrps = null;
	
	//**Accesseurs*/
	public int getUpdatetime() {return updatetime;}
	public String getTimezone() {return timezone;}
	public List<Measuregrp> getMeasuregrps() {return measuregrps;}

	//**Initialisateurs*/
	public void setUpdatetime(int updatetime) {this.updatetime = updatetime;}
	public void setTimezone(String timezone) {this.timezone = timezone;}
	public void setMeasuregrps(List<Measuregrp> measuregrps) {this.measuregrps = measuregrps;}

}
