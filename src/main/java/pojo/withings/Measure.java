package pojo.withings;

public class Measure {
	
	private int value;
	private int type;
	private int unit;

	//**Accesseurs*/
	public int getValue() {return value;}
	public int getType() {return type;}
	public int getUnit() {return unit;}

	//**initialisateurs*/
	public void setValue(int value) {this.value = value;}
	public void setType(int type) {this.type = type;}
	public void setUnit(int unit) {this.unit = unit;}
}
