package metier.withings;

import com.github.scribejava.core.oauth.OAuth20Service;

import domaine.oauth2.Oauth2AccessToken;
import domaine.oauth2.Oauth2Authorisation;
import metier.Plugin;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;

import outils.Constant;
import outils.SymmetricAESKey;
import pojo.withings.ActivityMeasures;

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
	
	public static Response getHearthRate(String encryptToken, String startDate, String endDate) {
		String url = String.format(Constant.WITHINGS_PROTECTED_RESOURCE_HEARTH_RATE_URL,startDate,endDate);
		LOG.log(Level.INFO,"URL : " + url);
		Response response = requestData(SymmetricAESKey.decrypt(encryptToken), getService(), ActivityMeasures.class, Verb.GET, url);
		return response;
	}
	
//	public static Response getActivityMeasures(String token) {
//		String url = String.format(Constant.FITBIT_PROTECTED_RESOURCE_HEARTH_RATE_URL);
//		LOG.log(Level.INFO,"URL : " + url);
//		Response response = requestData(token, getService(), HearthRateInterval.class, Verb.GET, url);
//		return response;
//	}
	
	public static <T> Response requestData (String token, OAuth20Service service, Class<T> classT, Verb verb, String urlRequest) {
		
		LOG.log(Level.INFO,String.format("Generate request with %s to URL : %s",verb,urlRequest));
		LOG.log(Level.INFO,"Generate request... ");
		OAuthRequest request = new OAuthRequest(verb, urlRequest);
        request.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN, token);
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
		if (response.getCode() == Response.Status.OK.getStatusCode()) {
			// TODO: ATTENTION IL FAUT ENVOYER LE JSON PARSE COMME POUR FITBIT
			T entityT = Plugin.unMarshallGenericJSON("", classT);
			return Response
					.status(response.getCode())
					.entity(entityT)
					.build();
		} else {
			String body = "error";
			try {
				body = response.getBody();
			} catch (Exception e) {
				LOG.log(Level.SEVERE,e.getMessage(),e);
			}
			return Response
					.status(response.getCode())
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
