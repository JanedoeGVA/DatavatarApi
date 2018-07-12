package metier.fitbit;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import domaine.oauth.ProtectedDataOauth;
import domaine.oauth2.Oauth2AccessToken;
import domaine.oauth2.Oauth2Authorisation;
import domaine.oauth2.ProtectedDataOauth2;
import metier.Plugin;
import outils.Constant;
import outils.SymmetricAESKey;
import pojo.fitbit.Profil;


public class FitbitPlugin {
	
	public static Oauth2Authorisation urlVerification() {
		Oauth2Authorisation oauth2Auth = Plugin.oauth20UrlVerification(Constant.FITBIT_API_NAME, getService());
        return oauth2Auth;
	}	    
    
	public static Oauth2AccessToken accessToken (String code) {
    	Oauth2AccessToken accessToken = Plugin.oauth20AccessToken(Constant.FITBIT_API_NAME,code, getService());
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
    
    public static <T> ProtectedDataOauth<T,Oauth2AccessToken> getGenericProtectedRessources(Oauth2AccessToken accessToken, OAuth20Service service, Class<T> classT, Verb verb, String urlRequest) { 
        OAuthRequest request = new OAuthRequest(verb, urlRequest);
        request.addHeader("x-li-format", "json");
        //add header for authentication (Fitbit complication..... :()
        request.addHeader("Authorization", "Bearer " + SymmetricAESKey.decrypt(accessToken.getAccessTokenKey()));
        Response response = null;
		try {
			response = service.execute(request);
		} catch (InterruptedException | ExecutionException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        System.out.println("Response code/message : " + response.getCode() + response.getMessage());
        ProtectedDataOauth<T,Oauth2AccessToken> protectedDataOauth = new ProtectedDataOauth<>();
        if (response.getCode() == javax.ws.rs.core.Response.Status.UNAUTHORIZED.getStatusCode()) {
        	System.out.println("Refreshing processing...");
        	Plugin.refreshAccessToken(accessToken, service);
        	if (accessToken.getIsValide()) {
        		request = new OAuthRequest(verb, urlRequest);
                request.addHeader("x-li-format", "json");
                //add header for authentication (Fitbit complication..... :()
                request.addHeader("Authorization", "Bearer " + SymmetricAESKey.decrypt(accessToken.getAccessTokenKey()));
            	try {
    				response = service.execute(request);
    			} catch (InterruptedException | ExecutionException | IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        	} else {
        		protectedDataOauth.setOauthAccessTokenT(accessToken);
        		System.out.println("Invalide token...");
        		return protectedDataOauth;
        	}
        }
        T t = Plugin.unMarshallGenericJSON(response,classT);
        protectedDataOauth.setOauthAccessTokenT(accessToken);
        protectedDataOauth.setProtectedDataT(t);
        System.out.println("is valide : " + accessToken.getIsValide()); 
        return protectedDataOauth;
	}
    
    public static void revoke (Oauth2AccessToken oauth2AccessToken) {
    	Plugin.revoke(oauth2AccessToken.getAccessTokenKey(), getService());
    }
}
