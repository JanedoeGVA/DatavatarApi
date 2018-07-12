package outils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.math3.analysis.function.Log;
import org.json.JSONObject;

import com.cars.framework.secrets.DockerSecretLoadException;
import com.cars.framework.secrets.DockerSecrets;
import com.fasterxml.jackson.core.JsonParser;
import com.google.gson.JsonElement;

public class Utils {
	
	public static String getProps (String propsName, String value) {
		try {
			Map<String, String> secrets = DockerSecrets.loadFromFile(propsName);
			return secrets.get(value);
		} catch (DockerSecretLoadException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static ArrayList<String> lstElement;
	
	public static Properties filesProperties (String path) {
		InputStream is = Utils.class.getClassLoader().getResourceAsStream(path);
		Properties config = new Properties();
		try {
			config.load(is);
		} catch (IOException e) {
			System.out.println("Could not load properties from " + path);
			e.printStackTrace();
			return null;
		}
		return config;
	}
	
	public static String getDecodedJWT(String jwt) {
	  String result = "";
	  String[] parts = jwt.split("[.]");
	  try {
	    int index = 0;
	    for(String part: parts) {
	      if (index >= 2) {break;}
	      index++;
	      byte[] partAsBytes = part.getBytes("UTF-8");
	      String decodedPart = new String(java.util.Base64.getUrlDecoder().decode(partAsBytes), "UTF-8");
	      result += decodedPart;
	    }
	  } catch(Exception e) {
		  throw new RuntimeException("Couldnt decode jwt", e);  
	  }
	  return result;
	}
	
	public static JSONObject getJSONDecodedAccessToken(String accessToken) {
		String result = null;
		String[] parts = accessToken.split("[.]");
		try {
			byte[] partAsBytes = parts[0].getBytes("UTF-8");
			String decodedPart = new String(java.util.Base64.getUrlDecoder().decode(partAsBytes), "UTF-8");
			result = "{\"part_01\":" + decodedPart + ",";
			partAsBytes = parts[1].getBytes("UTF-8");
			decodedPart = new String(java.util.Base64.getUrlDecoder().decode(partAsBytes), "UTF-8");
			result += result = "\"part_02\":" + decodedPart + "}";
		  } catch(Exception e) {
			  throw new RuntimeException("Couldnt decode jwt", e);  
		  }
		System.out.println(result);
		JSONObject jsonObject = new JSONObject(result);
		return jsonObject;
	}
	
	public static ArrayList<String> findValueJSON (String key,String json) {
		 com.google.gson.JsonParser jsParser = new com.google.gson.JsonParser();
		 JsonElement jsonElement = jsParser.parse(json);
		 lstElement = new ArrayList<String>(); 
		 checkJSON(key,jsonElement);
		 return lstElement;	 	
	}
	
	
	 private static void checkJSON (String key, JsonElement jsonElement) {
		 if (jsonElement.isJsonArray()) {
			 for (JsonElement jsonElement1 : jsonElement.getAsJsonArray()) {
				 checkJSON(key, jsonElement1);
			 }  
		 } else {
			 if (jsonElement.isJsonObject()) {
				 Set<Map.Entry<String, JsonElement>> entrySet = jsonElement.getAsJsonObject().entrySet();        
				 for (Map.Entry<String, JsonElement> entry : entrySet) {
					 String key1 = entry.getKey();
					 if (key1.equals(key)) { 
						 lstElement.add(entry.getValue().toString());
					 }
					 checkJSON(key, entry.getValue());
				 }
			 } else {       
				 if (jsonElement.toString().equals(key)) {   
					 lstElement.add(jsonElement.toString());
				 }
			 }
		 }
	 }

	
	
	

}
