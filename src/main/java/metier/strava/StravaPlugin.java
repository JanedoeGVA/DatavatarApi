package metier.strava;

import com.github.scribejava.core.oauth.OAuth20Service;

import domaine.oauth2.Oauth2AccessToken;
import domaine.oauth2.Oauth2Authorisation;
import metier.Plugin;
import outils.Constant;

public class StravaPlugin {
	
	public static Oauth2Authorisation urlVerification() {
		Oauth2Authorisation oauth2Auth = Plugin.oauth20UrlVerification(Constant.STRAVA_PROVIDER, getService());
        return oauth2Auth;
	}
	
	public static Oauth2AccessToken accessToken (String code) {
    	Oauth2AccessToken accessToken = Plugin.oauth20AccessToken(Constant.STRAVA_PROVIDER,code, getService());
    	return accessToken;
    }
	
	private static OAuth20Service getService() {
    	final OAuth20Service service = Plugin.getOauth2Service(Constant.STRAVA_PROPS,Constant.STRAVA_CALLBACK_URL,StravaApi_OAuth20.instance());
    	return service;
    }
}
