package metier.nokiahealth;

import com.github.scribejava.core.oauth.OAuth10aService;

import domaine.oauth.ProtectedDataOauth;
import domaine.oauth1a.Oauth1AccessToken;
import domaine.oauth1a.Oauth1Authorisation;
import metier.Plugin;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;

import outils.Constant;
import outils.SymmetricAESKey;
import pojo.nokiahealth.ActivityMeasures;

public class NokiaPlugin {
	
	public static Oauth1Authorisation getOauth1Authorisation() {
		Oauth1Authorisation oauth1Auth = Plugin.oauth10Authorisation(Constant.NOKIA_HEALTH_API_NAME, getService());
		return oauth1Auth;
	}
	
	public static Oauth1AccessToken getAccessToken(String requestTokenKey,String encryptedRequestTokenSecret,String verifier) {
		Oauth1AccessToken accessToken = Plugin.oauth10AccessToken(Constant.NOKIA_HEALTH_API_NAME,requestTokenKey, encryptedRequestTokenSecret, verifier, getService());
		return accessToken;
	}

	private static OAuth10aService getService() {
		final OAuth10aService service = Plugin.getOauth1Service(Constant.NOKIA_HEALTH_PROPS,Constant.NOKIA_HEALTH_CALLBACK_URL,NokiaApi_OAouth10.instance());
		return service;
	}
	/*
	public static ProtectedDataOauth<ActivityMeasures, Oauth1AccessToken> getActivityMeasures(Oauth1AccessToken accessToken) {
		String url = "http://api.health.nokia.com/measure?action=getmeas";
		ProtectedDataOauth<ActivityMeasures, Oauth1AccessToken> protectedAct = Plugin.getGenericProtectedRessources(accessToken, getService(), ActivityMeasures.class, Verb.GET, url);
		return protectedAct;
	}*/
	
	public static ProtectedDataOauth<ActivityMeasures, Oauth1AccessToken> getActivityMeasures(Oauth1AccessToken accessToken) {
		String url = Constant.NOKIAHEALTH_ACTIVITIES;
		ProtectedDataOauth<ActivityMeasures, Oauth1AccessToken> protectedAct = getProtectedDataT(accessToken, getService(), ActivityMeasures.class, url);
		return protectedAct;
	}
	
	private static <T>ProtectedDataOauth<T, Oauth1AccessToken> getProtectedDataT(Oauth1AccessToken accessToken,OAuth10aService service, Class<T> classT, String urlRequest) {
		final OAuth1AccessToken oAuth1AccessToken = new OAuth1AccessToken(SymmetricAESKey.decrypt(accessToken.getAccessTokenKey()),SymmetricAESKey.decrypt(accessToken.getAccessTokenSecret()));
		final OAuthRequest request = new OAuthRequest(Verb.GET, urlRequest);
		service.signRequest(oAuth1AccessToken, request);
	    Response response = null;
	    ProtectedDataOauth<T, Oauth1AccessToken> protectedDataOauth = new ProtectedDataOauth<>();
	    protectedDataOauth.setOauthAccessTokenT(accessToken);
		try {
			response = service.execute(request);
			System.out.println("response " + response.getCode() + response.getBody());
			if (new JSONObject(response.getBody()).getInt("status") == Constant.STATUS_TOKEN_NOT_FIND) {
				//token a �t� r�voqu�
				protectedDataOauth.getOauthAccessTokenT().setIsValide(false);
				return protectedDataOauth;
			}
		} catch (InterruptedException | ExecutionException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    T t = Plugin.unMarshallGenericJSON(response,classT);
	    protectedDataOauth.setOauthAccessTokenT(accessToken);
	    protectedDataOauth.setProtectedDataT(t);
	    return protectedDataOauth;
	}
	
}
