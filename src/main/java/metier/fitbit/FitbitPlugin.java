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
		Oauth2Authorisation oauth2Auth = Plugin.oauth20UrlVerification(Constant.FITBIT_PROVIDER, getService());

		return oauth2Auth;
	}	    

	public static Oauth2AccessToken accessToken (String code) {

		Oauth2AccessToken accessToken = Plugin.oauth20AccessToken(code, getService());
		return accessToken;
	}

	private static OAuth20Service getService() {
		final OAuth20Service service = Plugin.getOauth2Service(Constant.FITBIT_PROPS,Constant.FITBIT_CALLBACK_URL,FitbitApi_OAuth20.instance());
		return service;
	}
	//
	//	public static ProtectedDataOauth<Profil,Oauth2AccessToken> getProfil (Oauth2AccessToken accessToken) {
	//		String url =  Constant.FITBIT_PROTECTED_RESOURCE_PROFIL_URL;
	//		ProtectedDataOauth<Profil,Oauth2AccessToken> protectedProfil = getGenericProtectedRessources(accessToken, getService(), Profil.class, Verb.GET, url);
	//		return protectedProfil;
	//	} 

	//	public static ProtectedDataOauth<HearthRateInterval,Oauth2AccessToken> getHearthRate (Oauth2AccessToken accessToken, String date) {
	//		// String url =  String.format(Constant.FITBIT_PROTECTED_RESOURCE_HEARTH_RATE_URL,date);
	//		// String url = "https://api.fitbit.com/1/user/-/activities/heart/date/today/1d/1sec/time/12:05/12:06.json";
	//		String url = "https://api.fitbit.com/1/user/-/activities/heart/date/today/1d/1min.json";
	//		LOG.log(Level.INFO,"URL : " + url);
	//		ProtectedDataOauth<HearthRateInterval,Oauth2AccessToken> protectedHearthRate = getGenericProtectedRessources(accessToken, getService(), HearthRateInterval.class, Verb.GET, url);
	//		return protectedHearthRate;
	//	}

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
				long dt = Utils.convertDateHourToDateTime(dateTime, jsArray.getJSONObject(i).getString("time"));
				HeartRate hr = new HeartRate(value, dt);
				heartRateData.addHeartRateData(hr);
			}
			return heartRateData;
		} catch (JSONException | ParseException e) {
			throw new InvalidJSONException("error parsing", e);
		}
	}



	public static JSONObject requestData (OAuth20Service service, Verb verb, String urlRequest,ArrayList<Param> lstParams) throws UnAuthorizedException,ForbiddenException,IOException {
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
		final Oauth2AccessToken oauth2AccessToken = Plugin.refreshAccessToken(encryptRefreshToken, getService());
		return oauth2AccessToken;
	}

	public static void revoke (String token) throws IOException,InterruptedException, ExecutionException {
		LOG.log(Level.INFO,"revoking token");
			Plugin.revoke(token, getService());
	}

	//		LOG.log(Level.INFO,"Generate request... ");
	//		OAuthRequest request = new OAuthRequest(verb, urlRequest);
	//		LOG.log(Level.INFO,String.format("Access Token : key : %s , provider : %s , secret: %s , isValide : %s" , accessToken.getAccessTokenKey(),accessToken.getProvider(),accessToken.getRefreshTokenKey(),accessToken.getIsValide()));
	//		request.addHeader("x-li-format", "json");
	//		String token = SymmetricAESKey.decrypt(accessToken.getAccessTokenKey());
	//		request.addHeader("Authorization", "Bearer " + token);
	//		LOG.log(Level.INFO,"request : " + request.toString());
	//		com.github.scribejava.core.model.Response response = null;
	//		try {
	//			response = service.execute(request);
	//			LOG.log(Level.INFO,"Response success");
	//		} catch (InterruptedException | ExecutionException | IOException e1) {
	//			LOG.log(Level.SEVERE,e1.getMessage(),e1);
	//		} catch(Exception e) {
	//			LOG.log(Level.SEVERE,e.getMessage(),e);
	//		}
	//		LOG.log(Level.INFO,String.format("Response code/message : %s / %s",response.getCode(),response.getMessage()));
	//		try {
	//			LOG.log(Level.INFO,String.format("Body : %s ",response.getBody()));
	//		} catch (Exception e) {
	//			LOG.log(Level.SEVERE,e.getMessage(),e);
	//		}
	//		ProtectedDataOauth<T,Oauth2AccessToken> protectedDataOauth = new ProtectedDataOauth<>();
	//		if (response.getCode() == javax.ws.rs.core.Response.Status.UNAUTHORIZED.getStatusCode()) {
	//			LOG.log(Level.INFO,"Refreshing token processing...");
	//			Plugin.refreshAccessToken(accessToken, service);
	//			if (accessToken.getIsValide()) {
	//				request = new OAuthRequest(verb, urlRequest);
	//				request.addHeader("x-li-format", "json");
	//				//add header for authentication (Fitbit complication..... :()
	//				request.addHeader("Authorization", "Bearer " + SymmetricAESKey.decrypt(accessToken.getAccessTokenKey()));
	//				try {
	//					response = service.execute(request);
	//					LOG.log(Level.INFO,String.format("Response refresh token code/message : %s / %s",response.getCode(),response.getMessage()));
	//				} catch (InterruptedException | ExecutionException | IOException e) {
	//					LOG.log(Level.SEVERE,e.getMessage(),e);
	//				}
	//			} else {
	//				protectedDataOauth.setOauthAccessTokenT(accessToken);
	//				LOG.log(Level.INFO,"Invalid token");
	//				return protectedDataOauth;
	//			}
	//		}
	//		LOG.log(Level.INFO,"Response JSON = " + response);
	//		T t = Plugin.unMarshallGenericJSON(response,classT);
	//		protectedDataOauth.setOauthAccessTokenT(accessToken);
	//		protectedDataOauth.setProtectedDataT(t);
	//		LOG.log(Level.INFO,"token isValide : " + accessToken.getIsValide());
	//		return protectedDataOauth;
	// }

	//		public static <T> ProtectedDataOauth<T,Oauth2AccessToken> getGenericProtectedRessources(Oauth2AccessToken accessToken, OAuth20Service service, Class<T> classT, Verb verb, String urlRequest) { 
	//			LOG.log(Level.INFO,String.format("Generate request with %s to URL : %s",verb,urlRequest));
	//			LOG.log(Level.INFO,"Generate request... ");
	//			OAuthRequest request = new OAuthRequest(verb, urlRequest);
	//			LOG.log(Level.INFO,String.format("Access Token : key : %s , provider : %s , secret: %s , isValide : %s" , accessToken.getAccessTokenKey(),accessToken.getProvider(),accessToken.getRefreshTokenKey(),accessToken.getIsValide()));
	//			request.addHeader("x-li-format", "json");
	//			String token = SymmetricAESKey.decrypt(accessToken.getAccessTokenKey());
	//			request.addHeader("Authorization", "Bearer " + token);
	//			LOG.log(Level.INFO,"request : " + request.toString());
	//			com.github.scribejava.core.model.Response response = null;
	//			try {
	//				response = service.execute(request);
	//				LOG.log(Level.INFO,"Response success");
	//			} catch (InterruptedException | ExecutionException | IOException e1) {
	//				LOG.log(Level.SEVERE,e1.getMessage(),e1);
	//			} catch(Exception e) {
	//				LOG.log(Level.SEVERE,e.getMessage(),e);
	//			}
	//			LOG.log(Level.INFO,String.format("Response code/message : %s / %s",response.getCode(),response.getMessage()));
	//			try {
	//				LOG.log(Level.INFO,String.format("Body : %s ",response.getBody()));
	//			} catch (Exception e) {
	//				LOG.log(Level.SEVERE,e.getMessage(),e);
	//			}
	//			ProtectedDataOauth<T,Oauth2AccessToken> protectedDataOauth = new ProtectedDataOauth<>();
	//			if (response.getCode() == javax.ws.rs.core.Response.Status.UNAUTHORIZED.getStatusCode()) {
	//				LOG.log(Level.INFO,"Refreshing token processing...");
	//				Plugin.refreshAccessToken(accessToken, service);
	//				if (accessToken.getIsValide()) {
	//					request = new OAuthRequest(verb, urlRequest);
	//					request.addHeader("x-li-format", "json");
	//					//add header for authentication (Fitbit complication..... :()
	//					request.addHeader("Authorization", "Bearer " + SymmetricAESKey.decrypt(accessToken.getAccessTokenKey()));
	//					try {
	//						response = service.execute(request);
	//						LOG.log(Level.INFO,String.format("Response refresh token code/message : %s / %s",response.getCode(),response.getMessage()));
	//					} catch (InterruptedException | ExecutionException | IOException e) {
	//						LOG.log(Level.SEVERE,e.getMessage(),e);
	//					}
	//				} else {
	//					protectedDataOauth.setOauthAccessTokenT(accessToken);
	//					LOG.log(Level.INFO,"Invalid token");
	//					return protectedDataOauth;
	//				}
	//			}
	//			LOG.log(Level.INFO,"Response JSON = " + response);
	//			T t = Plugin.unMarshallGenericJSON(response,classT);
	//			protectedDataOauth.setOauthAccessTokenT(accessToken);
	//			protectedDataOauth.setProtectedDataT(t);
	//			LOG.log(Level.INFO,"token isValide : " + accessToken.getIsValide());
	//			return protectedDataOauth;
	//		}


}
