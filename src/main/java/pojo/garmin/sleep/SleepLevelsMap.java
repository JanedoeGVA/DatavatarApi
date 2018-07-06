package pojo.garmin.sleep;

import java.util.List;

public class SleepLevelsMap {
	
	private List<Deep> deep = null;
    private List<Light> light = null;
    private List<Awake> awake = null;

    public List<Deep> getDeep() {return deep;}
    public List<Light> getLight() {return light;}
    public List<Awake> getAwake() {return awake;}
    
    public void setDeep(List<Deep> deep) {this.deep = deep;}
    public void setLight(List<Light> light) {this.light = light;}
    public void setAwake(List<Awake> awake) {this.awake = awake;}
}
