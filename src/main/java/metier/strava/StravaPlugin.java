package metier.strava;

import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import domaine.Activity;
import domaine.Param;
import domaine.oauth2.Oauth2AccessToken;
import domaine.oauth2.Oauth2Authorisation;
import metier.Plugin;
import metier.exception.UnAuthorizedException;
import org.json.JSONArray;
import org.json.JSONObject;
import outils.Constant;
import outils.SymmetricAESKey;
import outils.Utils;
import pojo.HeartRate;
import pojo.HeartRateData;

import javax.ws.rs.ForbiddenException;
import java.io.IOException;

import java.text.ParseException;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static domaine.Param.TypeParam.QUERY_PARAM;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

public class StravaPlugin {

	private static final Logger LOG = Logger.getLogger(StravaPlugin.class.getName());
	
	public static Oauth2Authorisation urlVerification() {
		Oauth2Authorisation oauth2Auth = Plugin.oauth20UrlVerification(Constant.STRAVA_PROVIDER, getService());
        return oauth2Auth;
	}
	
	public static Oauth2AccessToken accessToken (String code) {
    	Oauth2AccessToken accessToken = Plugin.oauth20AccessToken(code, getService());
    	return accessToken;
    }
	
	private static OAuth20Service getService() {
    	final OAuth20Service service = Plugin.getOauth2Service(Constant.STRAVA_PROPS,Constant.STRAVA_CALLBACK_URL,StravaApi_OAuth20.instance());
    	return service;
    }

	public static Oauth2AccessToken refresh (String encryptRefreshToken) {
		final Oauth2AccessToken oauth2AccessToken = Plugin.refreshAccessToken(encryptRefreshToken, getService());
		return oauth2AccessToken;
	}

	public static void revoke (String token) throws IOException,InterruptedException,ExecutionException {
		LOG.log(Level.INFO,"revoking token");
		Plugin.revoke(token, getService());
	}

	private static HeartRateData parseHeartRate(JSONArray jsonArray,long startDateGMT)  {
		HeartRateData heartRateData = new HeartRateData();
		LOG.log(Level.INFO, "json : " + jsonArray.toString());
		final HashMap<String,JSONArray> hmData = new HashMap<>();
		jsonArray.forEach(item -> {
			final JSONObject jsonObject = (JSONObject) item;
			final String type = jsonObject.getString("type");
			final JSONArray jsData = jsonObject.getJSONArray("data");
			hmData.put(type,jsData);
		});
		final JSONArray jsArrTime = hmData.get("time");
		final JSONArray jsArrHR = hmData.get("heartrate");
		for (int i = 0; i <  jsArrTime.length(); i++) {
			final int hr = jsArrHR.getInt(i);
			final long t = jsArrTime.getInt(i) + startDateGMT;
			final HeartRate heartRate = new HeartRate(hr,t);
			heartRateData.addHeartRateData(heartRate);
		}
		return heartRateData;
	}

	private static ArrayList<Activity> parseListActivities(JSONArray jsonArray)  {
		LOG.log(Level.INFO, "json : " + jsonArray.toString());
		ArrayList<Activity> lstActivities = new ArrayList<>();
		jsonArray.forEach( item -> {
			JSONObject jsonObject = (JSONObject) item;

			final int id = jsonObject.getInt("id");
			final long date;
			try {
				date = Utils.convertDateUTCToDateTime(jsonObject.getString("start_date"));
				Activity activity = new Activity(id,date);
				lstActivities.add(activity);
			} catch (ParseException e) {
				LOG.log(Level.SEVERE,e.getMessage());
			}
		});
		return lstActivities;
	}



	public static HeartRateData getHeartRate(ArrayList<Activity> lstActivitiesId,String encryptToken) throws IOException, UnAuthorizedException, ForbiddenException {
		final ArrayList<Param> lstParams = new ArrayList<>();
		lstParams.add(new Param("keys","heartrate,time",QUERY_PARAM));
		lstParams.add(new Param(OAuthConstants.HEADER, "Bearer " + SymmetricAESKey.decrypt(encryptToken),Param.TypeParam.HEADER_PARAM));
		final HeartRateData heartRateData = new HeartRateData();
		for (Activity activity : lstActivitiesId) {
			final String url = String.format("https://www.strava.com/api/v3/activities/%s/streams",activity.getId());
			String body = requestData(getService(), Verb.GET, url,lstParams);
			JSONArray jsonArray = new JSONArray(body);
			final HeartRateData hrData = parseHeartRate(jsonArray,activity.getDate());
			heartRateData.getLstHeartRate().addAll(hrData.getLstHeartRate());
		}
		return heartRateData;
	}

	public static ArrayList<Activity> ListActivitiesId(long startDate, long endDate,String encryptToken) throws IOException, UnAuthorizedException, ForbiddenException {
		final ArrayList<Param> lstParams = new ArrayList<>();
		LOG.log(Level.INFO, "startDate : " + startDate);
		LOG.log(Level.INFO, "endDate : " + endDate);
		lstParams.add(new Param(Constant.STRAVA_PARAM_BEFORE,String.valueOf(endDate),QUERY_PARAM));
		lstParams.add(new Param(Constant.STRAVA_PARAM_AFTER,String.valueOf(startDate),QUERY_PARAM));
		lstParams.add(new Param(OAuthConstants.HEADER, "Bearer " + SymmetricAESKey.decrypt(encryptToken),Param.TypeParam.HEADER_PARAM));
		String responseBody = requestData(getService(), Verb.GET, "https://www.strava.com/api/v3/athlete/activities",lstParams);
		JSONArray jsonArray = new JSONArray(responseBody);
		return parseListActivities(jsonArray);
	}

	public static String requestData (OAuth20Service service, Verb verb, String urlRequest,ArrayList<Param> lstParams) throws IOException, UnAuthorizedException, ForbiddenException {
		LOG.log(Level.INFO,String.format("Generate request with %s to URL : %s",verb,urlRequest));
		LOG.log(Level.INFO,"Generate request... ");
		com.github.scribejava.core.model.Response response = Plugin.getResponse(service,urlRequest,verb,lstParams);
		final int code = response.getCode();
		if (code == OK.getStatusCode()) {
			try {
				String body = response.getBody();
				LOG.log(Level.INFO,String.format("Response body : %s  ",body));
				return body;
			} catch (IOException e) {
				LOG.log(Level.WARNING,e.getMessage(),e);
				throw new IOException();
			}
		} else if (code == UNAUTHORIZED.getStatusCode()) {
			throw new UnAuthorizedException("");
		} else {
			throw new ForbiddenException();
		}
	}


}
