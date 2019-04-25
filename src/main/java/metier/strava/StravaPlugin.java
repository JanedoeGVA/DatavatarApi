package metier.strava;

import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import domaine.Param;
import domaine.oauth2.Oauth2AccessToken;
import domaine.oauth2.Oauth2Authorisation;
import metier.Plugin;
import metier.exception.UnAuthorizedException;
import org.json.JSONObject;
import outils.Constant;
import outils.SymmetricAESKey;
import outils.Utils;
import pojo.HeartRateData;

import javax.ws.rs.ForbiddenException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

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

	private static HeartRateData parseHeartRate(JSONObject jsonObject)  {
		HeartRateData heartRateData = new HeartRateData();
		// TODO
		LOG.log(Level.INFO, "json : " + jsonObject.toString());
		// TODO
		return heartRateData;
	}

	public static HeartRateData getHeartRate(String encryptToken, long startDate, long endDate) throws IOException, UnAuthorizedException, ForbiddenException {
		final ArrayList<Param> lstParams = new ArrayList<>();
		LOG.log(Level.INFO, "startDate : " + startDate);
		LOG.log(Level.INFO, "endDate : " + endDate);
		// lstParams.add(new Param(Constant.STRAVA_PARAM_BEFORE,String.valueOf(endDate),QUERY_PARAM));
		// lstParams.add(new Param(Constant.STRAVA_PARAM_AFTER,String.valueOf(startDate),QUERY_PARAM));
		lstParams.add(new Param(OAuthConstants.HEADER, "Bearer " + SymmetricAESKey.decrypt(encryptToken),Param.TypeParam.HEADER_PARAM));
		LOG.log(Level.INFO,"TOKEN : " + SymmetricAESKey.decrypt(encryptToken));
		JSONObject jsonObject = requestData(getService(), Verb.GET, "https://www.strava.com/api/v3/activities/2312724975/streams",lstParams);
		return parseHeartRate(jsonObject);
	}

	public static JSONObject requestData (OAuth20Service service, Verb verb, String urlRequest,ArrayList<Param> lstParams) throws IOException, UnAuthorizedException, ForbiddenException {
		LOG.log(Level.INFO,String.format("Generate request with %s to URL : %s",verb,urlRequest));
		LOG.log(Level.INFO,"Generate request... ");
		OAuthRequest request = new OAuthRequest(verb, urlRequest);
		for (Param param : lstParams) {
			if (param.getType() == Param.TypeParam.QUERY_PARAM) {
				request.addQuerystringParameter(param.getKey(), param.getValue());
			} else {
				request.addHeader(param.getKey(), param.getValue());
			}

		}
		LOG.log(Level.INFO,"request : " + request.toString());
		com.github.scribejava.core.model.Response response = null;
		try {
			response = service.execute(request);
			LOG.log(Level.INFO,"Response success");
		} catch (InterruptedException | ExecutionException | IOException e1) {
			LOG.log(Level.SEVERE,e1.getMessage(),e1);
		} catch(Exception e) {
			LOG.log(Level.SEVERE,e.getMessage(),e);
		}
		LOG.log(Level.INFO,String.format("Response code/message : %s / %s",response.getCode(),response.getMessage()));
		final int code = response.getCode();
		if (code == OK.getStatusCode()) {
			try {
				String body = response.getBody();
				LOG.log(Level.INFO,String.format("Response body : %s  ",body));
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


}
