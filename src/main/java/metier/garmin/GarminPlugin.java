package metier.garmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.InternalServerErrorException;
import javax.xml.bind.JAXBException;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import domaine.authorization.ProtectedListDataOauth;
import domaine.authorization.RequestProtectedData;
import domaine.oauth1a.Oauth1AccessToken;
import domaine.oauth1a.Oauth1Authorisation;
import metier.Plugin;
import metier.exception.InvalidJSONException;
import metier.exception.UnAuthorizedException;
import net.oauth.OAuthException;
import org.json.JSONObject;
import outils.Constant;
import outils.SymmetricAESKey;
import pojo.garmin.Epoch;
import pojo.garmin.sleep.Sleep;


public class GarminPlugin {

	private static final Logger LOG = Logger.getLogger(GarminPlugin.class.getName());
	
	public static Oauth1Authorisation getOauth1Authorisation() {
		Oauth1Authorisation oauth1Auth = Plugin.oauth10Authorisation(Constant.GARMIN_PROVIDER, getService());
		return oauth1Auth;
	}
	
	public static Oauth1AccessToken getAccessToken(String requestTokenKey,String encryptedRequestTokenSecret,String verifier) {
		Oauth1AccessToken accessToken = Plugin.oauth10AccessToken(requestTokenKey, encryptedRequestTokenSecret, verifier, getService());
		return accessToken;
	}
	
//	public static ProtectedListDataOauth<Epoch, Oauth1AccessToken> protectedEpoch(RequestProtectedData<Oauth1AccessToken> requestProtectedData) {
//		String url = Constant.GARMIN_EPOCHS;
//		ProtectedListDataOauth<Epoch,Oauth1AccessToken> protectedEpoch = getGenericListProtectedRessources(requestProtectedData,getService(),Verb.GET,Epoch.class,url);
//		return protectedEpoch;
//	}
	
//	public static ProtectedListDataOauth<Sleep,Oauth1AccessToken> protectedSleep(RequestProtectedData<Oauth1AccessToken> requestProtectedData) {
//		String url = Constant.GARMIN_SLEEPS;
//		ProtectedListDataOauth<Sleep,Oauth1AccessToken> protectedSleep = getGenericListProtectedRessources(requestProtectedData,getService(),Verb.GET,Sleep.class,url);
//		return protectedSleep;
//	}

	public static Epoch protectedEpoch(long startTime,long endTime,String encryptedToken,String encryptSecret) throws UnAuthorizedException {
		String url = String.format(Constant.GARMIN_EPOCHS,startTime,endTime);
		JSONObject jsObj = getProtectedRessources(url,encryptedToken,encryptSecret,getService(),Verb.GET);
		return null;
	}



	
	public static JSONObject getProtectedRessources(String url, String encryptToken,String encryptSecret ,OAuth10aService service, Verb verb) throws UnAuthorizedException,InternalServerErrorException {
		final OAuth1AccessToken oAuth1AccessToken = new OAuth1AccessToken(SymmetricAESKey.decrypt(encryptToken),SymmetricAESKey.decrypt(encryptSecret));
		final OAuthRequest request = new OAuthRequest(verb, url);
	    service.signRequest(oAuth1AccessToken, request);
		try {
			Response resp = service.execute(request);
		    if (resp.getCode() == javax.ws.rs.core.Response.Status.FORBIDDEN.getStatusCode()) { //error 403 token has been revoke
		    	throw new UnAuthorizedException("Token has been revoked");
		    }
			LOG.log(Level.INFO,"Request code : " + resp.getCode());
			String body = resp.getBody();
			LOG.log(Level.INFO,"Request body : " + body);
			JSONObject js = new JSONObject(body);
			return js;
		} catch (InterruptedException | ExecutionException | IOException e1) {
			throw new InternalServerErrorException("Error during request",e1);
		}
	}
	
	private static OAuth10aService getService() {
		final OAuth10aService service = Plugin.getOauth1Service(Constant.GARMIN_PROPS,Constant.GARMIN_CALLBACK_URL,GarminApi_OAouth10.instance());
		return service;
	}

	public static void revoke(Oauth1AccessToken oauth1AccessToken) {
		OAuthRequest request = new OAuthRequest(Verb.DELETE,"https://healthapi.garmin.com/wellness-api/rest/user/registration");
		OAuth1AccessToken accessToken = new OAuth1AccessToken(SymmetricAESKey.decrypt(oauth1AccessToken.getAccessToken()), SymmetricAESKey.decrypt(oauth1AccessToken.getSecret()));
		getService().signRequest(accessToken, request);
		try {
			Response response =  getService().execute(request);
			System.out.println("code revoke = " + response.getCode() + response.getMessage());
		} catch (InterruptedException | ExecutionException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
