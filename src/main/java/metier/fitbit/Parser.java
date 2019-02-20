package metier.fitbit;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class Parser {
	
	public static JSONObject parseHearthRate(JSONObject json) {
		JSONObject jo = new JSONObject();
		ArrayList<Integer> lstHearthRate = new ArrayList<>();
		JSONArray array = json.getJSONObject("activities-heart-intraday").getJSONArray("dataset");
		
		for (int i=0; i < array.length(); i++) {
			lstHearthRate.add(array.getJSONObject(i).getInt("value"));
		}
		JSONArray jsArray = new JSONArray(lstHearthRate);
		jo.put("start",array.getJSONObject(0).getString("time"));
		jo.put("end",array.getJSONObject(array.length()-1).getString("time"));
		jo.put("lstHearthRate", jsArray);
		return jo;
		
	}

}
