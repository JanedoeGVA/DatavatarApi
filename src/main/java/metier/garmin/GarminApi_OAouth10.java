package metier.garmin;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.builder.api.OAuth1SignatureType;
import com.github.scribejava.core.model.OAuth1RequestToken;
import outils.Constant;

public class GarminApi_OAouth10 extends DefaultApi10a {
	
	private static class InstanceHolder {
		
        private static final GarminApi_OAouth10 INSTANCE = new GarminApi_OAouth10();
    }
	
	public static GarminApi_OAouth10 instance() {
        return InstanceHolder.INSTANCE;
    }
	
	@Override
	public OAuth1SignatureType getSignatureType() {
		/*Useless Header is default value */
		return OAuth1SignatureType.Header;
	}

	@Override
	public String getRequestTokenEndpoint() {
		return Constant.GARMIN_TOKEN_REQUEST_ENDPOINT_URL;
	}

	@Override
	public String getAccessTokenEndpoint() {
		return Constant.GARMIN_TOKEN_ACCES_ENDPOINT_URL;
	}

	@Override
	public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
		//DefaultApi10a getAuthorization(...) add what Garmin needed on the URl ( just the token)
		return super.getAuthorizationUrl(requestToken);
	}

	@Override
	protected String getAuthorizationBaseUrl() {
		return Constant.GARMIN_BASE_AUTH_URL;
	}
	
	
	
}
