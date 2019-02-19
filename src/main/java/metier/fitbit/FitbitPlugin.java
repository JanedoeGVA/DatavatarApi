package metier.fitbit;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import domaine.oauth.ProtectedDataOauth;
import domaine.oauth2.Oauth2AccessToken;
import domaine.oauth2.Oauth2Authorisation;
import domaine.oauth2.ProtectedDataOauth2;
import main.Fitbit;
import metier.Plugin;
import outils.Constant;
import outils.SymmetricAESKey;
import pojo.fitbit.Profil;
import pojo.fitbit.hearthrate.HearthRateInterval;


public class FitbitPlugin {
	
	private static final Logger LOG = Logger.getLogger(FitbitPlugin.class.getName());

	public static Oauth2Authorisation urlVerification() {
		Oauth2Authorisation oauth2Auth = Plugin.oauth20UrlVerification(Constant.FITBIT_PROVIDER, getService());
		return oauth2Auth;
	}	    

	public static Oauth2AccessToken accessToken (String code) {
		Oauth2AccessToken accessToken = Plugin.oauth20AccessToken(Constant.FITBIT_PROVIDER,code, getService());
		return accessToken;
	}

	private static OAuth20Service getService() {
		final OAuth20Service service = Plugin.getOauth2Service(Constant.FITBIT_PROPS,Constant.FITBIT_CALLBACK_URL,FitbitApi_OAuth20.instance());
		return service;
	}

	public static ProtectedDataOauth<Profil,Oauth2AccessToken> getProfil (Oauth2AccessToken accessToken) {
		String url =  Constant.FITBIT_PROTECTED_RESOURCE_PROFIL_URL;
		ProtectedDataOauth<Profil,Oauth2AccessToken> protectedProfil = getGenericProtectedRessources(accessToken, getService(), Profil.class, Verb.GET, url);
		return protectedProfil;
	} 

	public static ProtectedDataOauth<HearthRateInterval,Oauth2AccessToken> getHearthRate (Oauth2AccessToken accessToken, String date) {
		// String url =  String.format(Constant.FITBIT_PROTECTED_RESOURCE_HEARTH_RATE_URL,date);
		// String url = "https://api.fitbit.com/1/user/-/activities/heart/date/today/1d/1sec/time/12:05/12:06.json";
		String url = "https://api.fitbit.com/1/user/-/activities/heart/date/today/1d.json";
		LOG.log(Level.INFO,"URL : " + url);
		ProtectedDataOauth<HearthRateInterval,Oauth2AccessToken> protectedHearthRate = getGenericProtectedRessources(accessToken, getService(), HearthRateInterval.class, Verb.GET, url);
		return protectedHearthRate;
	}

	public static <T> ProtectedDataOauth<T,Oauth2AccessToken> getGenericProtectedRessources(Oauth2AccessToken accessToken, OAuth20Service service, Class<T> classT, Verb verb, String urlRequest) { 
		LOG.log(Level.INFO,String.format("Generate request with %s to URL : %s",verb,urlRequest));
		LOG.log(Level.INFO,"Generate request... ");
		OAuthRequest request = new OAuthRequest(verb, urlRequest);
		LOG.log(Level.INFO,String.format("Access Token : key : %s , provider : %s , secret: %s , isValide : %s" , accessToken.getAccessTokenKey(),accessToken.getProvider(),accessToken.getRefreshTokenKey(),accessToken.getIsValide()));
		request.addHeader("x-li-format", "json");
		// add header for authentication (Fitbit complication..... :()
		request.addHeader("Authorization", "Bearer " + SymmetricAESKey.decrypt(accessToken.getAccessTokenKey()));
		Response response = null;
		try {
			response = service.execute(request);
			LOG.log(Level.INFO,"Response success");
		} catch (InterruptedException | ExecutionException | IOException e1) {
			LOG.log(Level.SEVERE,e1.getMessage(),e1);
		}
		LOG.log(Level.INFO,String.format("Response code/message : %s / %s",response.getCode(),response.getMessage()));
		ProtectedDataOauth<T,Oauth2AccessToken> protectedDataOauth = new ProtectedDataOauth<>();
		if (response.getCode() == javax.ws.rs.core.Response.Status.UNAUTHORIZED.getStatusCode()) {
			LOG.log(Level.INFO,"Refreshing token processing...");
			Plugin.refreshAccessToken(accessToken, service);
			if (accessToken.getIsValide()) {
				request = new OAuthRequest(verb, urlRequest);
				request.addHeader("x-li-format", "json");
				//add header for authentication (Fitbit complication..... :()
				request.addHeader("Authorization", "Bearer " + SymmetricAESKey.decrypt(accessToken.getAccessTokenKey()));
				try {
					response = service.execute(request);
					LOG.log(Level.INFO,String.format("Response refresh token code/message : %s / %s",response.getCode(),response.getMessage()));
				} catch (InterruptedException | ExecutionException | IOException e) {
					LOG.log(Level.SEVERE,e.getMessage(),e);
				}
			} else {
				protectedDataOauth.setOauthAccessTokenT(accessToken);
				LOG.log(Level.INFO,"Invalid token");
				return protectedDataOauth;
			}
		}
		LOG.log(Level.INFO,"Response JSON = " + response);
		T t = Plugin.unMarshallGenericJSON(response,classT);
		protectedDataOauth.setOauthAccessTokenT(accessToken);
		protectedDataOauth.setProtectedDataT(t);
		LOG.log(Level.INFO,"token isValide : " + accessToken.getIsValide());
		return protectedDataOauth;
	}

	public static void revoke (Oauth2AccessToken oauth2AccessToken) {
		LOG.log(Level.INFO,"revoking token : " + oauth2AccessToken.getAccessTokenKey());
		Plugin.revoke(oauth2AccessToken.getAccessTokenKey(), getService());
	}
}
