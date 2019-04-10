package outils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.jersey.uri.UriTemplate;
import org.json.JSONObject;
import com.cars.framework.secrets.DockerSecretLoadException;
import com.cars.framework.secrets.DockerSecrets;

import com.google.gson.JsonElement;


import metier.Plugin;

import javax.ws.rs.core.UriBuilder;

public class Utils {
	
	private static final Logger LOG = Logger.getLogger(Plugin.class.getName());

	private static String DATE_PATTERN = "yyyy-MM-dd";

	private static DateFormat df = new SimpleDateFormat(DATE_PATTERN);

	public static URI formatUrl(String template,Map<String, String> parameters) {
		UriBuilder builder = UriBuilder.fromPath(template);
		URI uri = builder.buildFromMap(parameters);
		return uri;
	}


	public static String formatDateTime(long epoch) {
		Date date = new Date(epoch);
		return df.format(date);
	}

	public static String getProps (String propsName, String value) {
		try {
			Map<String, String> secrets = DockerSecrets.loadFromFile(propsName);
			return secrets.get(value);
		} catch (DockerSecretLoadException e) {
			LOG.log(Level.SEVERE,e.getMessage(),e);
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
			LOG.log(Level.SEVERE,e.getMessage(),e);
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
			  LOG.log(Level.SEVERE,e.getMessage(),e);
		  }
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
