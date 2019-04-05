package metier.withings;

import com.github.scribejava.core.oauth.OAuth20Service;

import com.google.gson.JsonObject;
import domaine.QueryParam;
import domaine.oauth2.Oauth2AccessToken;
import domaine.oauth2.Oauth2Authorisation;
import metier.Plugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;

import org.json.JSONArray;
import org.json.JSONObject;
import outils.Constant;
import outils.SymmetricAESKey;
import pojo.withings.ActivityMeasures;

import static org.eclipse.persistence.annotations.Convert.JSON;

public class WithingsPlugin {
	
	private static final Logger LOG = Logger.getLogger(WithingsPlugin.class.getName());
	
	public static Oauth2Authorisation urlVerification() {
		Oauth2Authorisation oauth2Auth = Plugin.oauth20UrlVerification(Constant.WITHINGS_PROVIDER, getService());
        return oauth2Auth;
	}
	
	public static Oauth2AccessToken accessToken (String code) {
    		Oauth2AccessToken accessToken = Plugin.oauth20AccessToken(code, getService());
    		return accessToken;
    }
	
	private static OAuth20Service getService() {
    		final OAuth20Service service = Plugin.getOauth2Service(Constant.WITHINGS_PROPS,Constant.WITHINGS_CALLBACK_URL,WithingsApi_Oauth20.instance());
    		return service;
    }
	
	public static Oauth2AccessToken refresh (String refreshToken) {
		Oauth2AccessToken oauth2AccessToken = Plugin.refreshAccessToken(refreshToken, getService());
		return oauth2AccessToken;
	}

//	public static ProtectedDataOauth<ActivityMeasures,Oauth2AccessToken> getActivityMeasures (Oauth2AccessToken accessToken,String startDate,String endDate) {
//		String url = String.format(Constant.WITHINGS_PROTECTED_RESOURCE_HEARTH_RATE_URL,startDate,endDate);
//		ProtectedDataOauth<ActivityMeasures,Oauth2AccessToken> activityMeasures = getGenericProtectedRessources(accessToken, getService(), ActivityMeasures.class, Verb.GET, url);
//    	return activityMeasures;
//    }
	
	public static Response getHearthRate(String encryptToken,String startDate,String endDate) {
		LOG.log(Level.INFO,"token : " + SymmetricAESKey.decrypt(encryptToken));
		final ArrayList<QueryParam> lstQueryParams = new ArrayList<>();
		lstQueryParams.add(new QueryParam(Constant.WITHINGS_PARAM_ACTION,Constant.WITHINGS_PARAM_ACTION_GETMEAS));
		lstQueryParams.add(new QueryParam(Constant.WITHINGS_PARAM_MEASTYPE,Constant.WITHINGS_PARAM_MEASTYPE_HR));
		lstQueryParams.add(new QueryParam(OAuthConstants.ACCESS_TOKEN,SymmetricAESKey.decrypt(encryptToken)));
		lstQueryParams.add(new QueryParam(Constant.WITHINGS_PARAM_START_DATE,startDate));
		lstQueryParams.add(new QueryParam(Constant.WITHINGS_PARAM_END_DATE,endDate));
		return requestData(getService(), ActivityMeasures.class, Verb.GET, Constant.WITHINGS_MEASURE_URL,lstQueryParams);
	}
	
//	public static Response getActivityMeasures(String token) {
//		String url = String.format(Constant.FITBIT_PROTECTED_RESOURCE_HEARTH_RATE_URL);
//		LOG.log(Level.INFO,"URL : " + url);
//		Response response = requestData(token, getService(), HearthRateInterval.class, Verb.GET, url);
//		return response;
//	}
	
	private static <T> Response requestData (OAuth20Service service, Class<T> classT, Verb verb, String urlRequest, ArrayList<QueryParam> lstQueryParams) {
		
		LOG.log(Level.INFO,String.format("Generate request with %s to URL : %s",verb,urlRequest));

		OAuthRequest request = new OAuthRequest(verb, urlRequest);

		for (QueryParam queryParam : lstQueryParams) {
			request.addQuerystringParameter(queryParam.getKey(),queryParam.getValue());
		}
		LOG.log(Level.INFO,"Request : "+ request.getCompleteUrl());
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
		String body = "{}";
		if (response.getCode() == Response.Status.OK.getStatusCode()) {
			try {
				body = response.getBody();
			} catch(Exception e) {
				LOG.log(Level.SEVERE,e.getMessage(),e);
			}
			JSONObject jsonObject = new JSONObject(body);
			final int status = jsonObject.getInt(Constant.WITHINGS_STATUS_CODE);
			LOG.log(Level.INFO,String.format("Response body : %s",body));
			LOG.log(Level.INFO,String.format("status : %s",status));
			if (status == 0) {
				LOG.log(Level.INFO,String.format("creating jsA : %s",status));
				JSONArray jsArrItems = new JSONArray();
				JSONArray jsArray = jsonObject.getJSONObject("body").getJSONArray("mesuregrps");
				LOG.log(Level.INFO,String.format("jsArray : %s",jsArray.length()));
				for (Object jsonObj : jsArray) {
					final JSONObject jsonMesure = ((JSONObject)jsonObj).getJSONArray("").getJSONObject(0);
					final int value =  jsonMesure.getInt("value")/100;
					LOG.log(Level.INFO,"value : " + value);
					jsArrItems.put(value);
				}

//				jsArray.forEach(item-> {
//					final JSONObject jsonMesure = ((JSONObject)item).getJSONArray("measures").getJSONObject(0);
//					final int value =  jsonMesure.getInt("value")/100;
//					LOG.log(Level.INFO,"value : " + value);
//					jsArrItems.put(value);
//				});



				// TODO: ATTENTION IL FAUT ENVOYER LE JSON PARSE COMME POUR FITBIT
				T entityT = Plugin.unMarshallGenericJSON("", classT);
				// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				return Response
						.status(Response.Status.OK.getStatusCode())
						.entity(entityT)
						.build();
			} else {
				int code;
				if (status == Response.Status.UNAUTHORIZED.getStatusCode()) {
					code = status;
				} else {
					code = Response.Status.BAD_REQUEST.getStatusCode();
				}
				return Response
						.status(code)
						.entity("{}")
						.build();
			}
		} else {
			try {
				body = response.getBody();
			} catch (Exception e) {
				LOG.log(Level.SEVERE,e.getMessage(),e);
			}
			int responseCode = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
			return Response
					.status(responseCode)
					.entity(body)
					.build();
		}	
	}
	
//	public static <T> ProtectedDataOauth<T,Oauth2AccessToken> getGenericProtectedRessources(Oauth2AccessToken accessToken, OAuth20Service service, Class<T> classT, Verb verb, String urlRequest) { 
//        OAuthRequest request = new OAuthRequest(verb, urlRequest);
//        request.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN, SymmetricAESKey.decrypt(accessToken.getAccessTokenKey()));
//        /* for getmeas
//        		&meastype=[INTEGER]
//        		&category=[INT]
//        		&startdate=[INT]
//        		&enddate=[INT]
//        		&offset=[INT]*/
//        Response response = null;
//		try {
//			response = service.execute(request);
//		} catch (InterruptedException | ExecutionException | IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//        System.out.println("Response code/message : " + response.getCode() + response.getMessage());
//        ProtectedDataOauth<T,Oauth2AccessToken> protectedDataOauth = new ProtectedDataOauth<>();
//        if (response.getCode() == javax.ws.rs.core.Response.Status.UNAUTHORIZED.getStatusCode()) {
//        	System.out.println("Refreshing processing...");
//        	Plugin.refreshAccessToken(accessToken, service);
//        	if (accessToken.getIsValide()) {
//        		request = new OAuthRequest(verb, urlRequest);
//                request.addHeader("x-li-format", "json");
//                //add header for authentication (Fitbit complication..... :()
//                request.addHeader("Authorization", "Bearer " + SymmetricAESKey.decrypt(accessToken.getAccessTokenKey()));
//            	try {
//    				response = service.execute(request);
//    			} catch (InterruptedException | ExecutionException | IOException e) {
//    				// TODO Auto-generated catch block
//    				e.printStackTrace();
//    			}
//        	} else {
//        		protectedDataOauth.setOauthAccessTokenT(accessToken);
//        		System.out.println("Invalide token...");
//        		return protectedDataOauth;
//        	}
//        }
//        T t = Plugin.unMarshallGenericJSON(response,classT);
//        protectedDataOauth.setOauthAccessTokenT(accessToken);
//        protectedDataOauth.setProtectedDataT(t);
//        System.out.println("is valide : " + accessToken.getIsValide()); 
//        return protectedDataOauth;
//	}
	
	/*public static ProtectedDataOauth<ActivityMeasures, Oauth1AccessToken> getActivityMeasures(Oauth1AccessToken accessToken) {
		String url = Constant.NOKIAHEALTH_ACTIVITIES;
		ProtectedDataOauth<ActivityMeasures, Oauth1AccessToken> protectedAct = getProtectedDataT(accessToken, getService(), ActivityMeasures.class, url);
		return protectedAct;
	}*/
	
//	private static <T>ProtectedDataOauth<T, Oauth1AccessToken> getProtectedDataT(Oauth1AccessToken accessToken,OAuth10aService service, Class<T> classT, String urlRequest) {
//		final OAuth1AccessToken oAuth1AccessToken = new OAuth1AccessToken(SymmetricAESKey.decrypt(accessToken.getAccessTokenKey()),SymmetricAESKey.decrypt(accessToken.getAccessTokenSecret()));
//		final OAuthRequest request = new OAuthRequest(Verb.GET, urlRequest);
//		service.signRequest(oAuth1AccessToken, request);
//	    Response response = null;
//	    ProtectedDataOauth<T, Oauth1AccessToken> protectedDataOauth = new ProtectedDataOauth<>();
//	    protectedDataOauth.setOauthAccessTokenT(accessToken);
//		try {
//			response = service.execute(request);
//			System.out.println("response " + response.getCode() + response.getBody());
//			if (new JSONObject(response.getBody()).getInt("status") == Constant.STATUS_TOKEN_NOT_FIND) {
//				//token a �t� r�voqu�
//				protectedDataOauth.getOauthAccessTokenT().setIsValide(false);
//				return protectedDataOauth;
//			}
//		} catch (InterruptedException | ExecutionException | IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//	    T t = Plugin.unMarshallGenericJSON(response,classT);
//	    protectedDataOauth.setOauthAccessTokenT(accessToken);
//	    protectedDataOauth.setProtectedDataT(t);
//	    return protectedDataOauth;
//	}
	
}
