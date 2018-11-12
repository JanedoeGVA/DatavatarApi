package pojo.withings;

import java.util.List;

public class Measuregrp {
	
	private int grpid;
	private int attrib;
	private int date;
	private int category;
	private List<Measure> measures = null;
	private String comment;
	
	//**Accesseurs*/
	public int getGrpid() {return grpid;}
	public int getAttrib() {return attrib;}
	public int getDate() {return date;}
	public int getCategory() {return category;}
	public List<Measure> getMeasures() {return measures;}
	public String getComment() {return comment;}
	
	//**Initialisateurs*/
	public void setGrpid(int grpid) {this.grpid = grpid;}
	public void setAttrib(int attrib) {this.attrib = attrib;}
	public void setDate(int date) {this.date = date;}
	public void setCategory(int category) {this.category = category;}
	public void setMeasures(List<Measure> measures) {this.measures = measures;}
	public void setComment(String comment) {this.comment = comment;}

}
