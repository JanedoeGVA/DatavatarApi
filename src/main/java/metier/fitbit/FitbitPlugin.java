package metier.fitbit;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.ForbiddenException;

import domaine.Param;
import metier.exception.InvalidJSONException;
import metier.exception.UnAuthorizedException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import domaine.oauth2.Oauth2AccessToken;
import domaine.oauth2.Oauth2Authorisation;
import metier.Plugin;
import outils.Constant;
import outils.SymmetricAESKey;
import outils.Utils;
import pojo.HeartRate;
import pojo.HeartRateData;


import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;


public class FitbitPlugin {

	private static final Logger LOG = Logger.getLogger(FitbitPlugin.class.getName());

	public static Oauth2Authorisation urlVerification() {
		LOG.log(Level.INFO,"urlVerification call");
		return Plugin.oauth20UrlVerification(Constant.FITBIT_PROVIDER, getService());
	}	    

	public static Oauth2AccessToken accessToken (String code) {
		return Plugin.oauth20AccessToken(code, getService());
	}

	private static OAuth20Service getService() {
		return Plugin.getOauth2Service(Constant.FITBIT_PROPS,Constant.FITBIT_CALLBACK_URL,FitbitApi_OAuth20.instance());
	}

	public static HeartRateData getHeartRate(String encryptToken, long startDate, long endDate) throws UnAuthorizedException,ForbiddenException,IOException,InvalidJSONException {
		Map<String, String> parameters = new HashMap<>();
		LOG.log(Level.INFO, "startDate : " + startDate);
		LOG.log(Level.INFO, "endDate : " + endDate);
		LOG.log(Level.INFO, " format startDate : " + Utils.formatDateTime(startDate));
		LOG.log(Level.INFO, "format endDate : " + Utils.formatDateTime(endDate));
		parameters.put(Constant.FITBIT_PARAM_DATE,Utils.formatDateTime(startDate));
		parameters.put(Constant.FITBIT_PARAM_END_DATE,"1d");
		parameters.put(Constant.FITBIT_PARAM_DETAIL_LVL,Constant.FITBIT_DETAIL_LVL_MIN);
		URI uri = Utils.formatUrl(Constant.FITBIT_TEMPLATE_HEART_RATE_URL,parameters);
		final ArrayList<Param> lstParams = new ArrayList<>();
		LOG.log(Level.INFO,"TOKEN : " + SymmetricAESKey.decrypt(encryptToken));
		lstParams.add(new Param(OAuthConstants.HEADER, "Bearer " + SymmetricAESKey.decrypt(encryptToken),Param.TypeParam.HEADER_PARAM));
		JSONObject jsonObject = requestData(getService(), Verb.GET, uri.toASCIIString(),lstParams);
		return parseHeartRate(jsonObject);
	}

	private static HeartRateData parseHeartRate(JSONObject jsonObject) throws InvalidJSONException {
		LOG.log(Level.INFO,"json to parse" + jsonObject.toString());
		try {
			HeartRateData heartRateData = new HeartRateData();
			JSONArray jsArray = jsonObject.getJSONObject("activities-heart-intraday").getJSONArray("dataset");
			String dateTime = jsonObject.getJSONArray("activities-heart").getJSONObject(0).getString("dateTime");
			if (jsArray.length() == 0) {
				return null;
			}
			for (int i = 0; i < jsArray.length(); i++) {
				int value = jsArray.getJSONObject(i).getInt("value");
				// BUG: 12:00 -> 0:00
				long dt = Utils.convertDateHourToDateTime(dateTime, jsArray.getJSONObject(i).getString("time"));
//				LOG.log(Level.INFO,"date : " + jsArray.getJSONObject(i).getString("time"));
//				LOG.log(Level.INFO,"dt : " + dt);
				HeartRate hr = new HeartRate(value, dt);
				heartRateData.addHeartRateData(hr);
			}
			return heartRateData;
		} catch (JSONException | ParseException e) {
			throw new InvalidJSONException("error parsing", e);
		}
	}

	private static JSONObject requestData (OAuth20Service service, Verb verb, String urlRequest,ArrayList<Param> lstParams) throws UnAuthorizedException,ForbiddenException,IOException {
		LOG.log(Level.INFO,String.format("Generate request with %s to URL : %s",verb,urlRequest));
		LOG.log(Level.INFO,"Generate request... ");
		com.github.scribejava.core.model.Response response = Plugin.getResponse(service,urlRequest,verb,lstParams);
		final int code = response.getCode();
		if (code == OK.getStatusCode()) {
			try {
				String body = response.getBody();
				JSONObject jsonObj = new JSONObject(body);
				return jsonObj;
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

	public static Oauth2AccessToken refresh (String encryptRefreshToken) {
		return Plugin.refreshAccessToken(encryptRefreshToken, getService());
	}

	public static void revoke (String token) throws IOException,InterruptedException, ExecutionException {
		LOG.log(Level.INFO,"revoking token");
			Plugin.revoke(token, getService());
	}


}
