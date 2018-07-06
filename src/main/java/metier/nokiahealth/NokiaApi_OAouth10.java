package metier.nokiahealth;

import java.util.Properties;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.builder.api.OAuth1SignatureType;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.ParameterList;

import outils.Constant;
import outils.Utils;

public class NokiaApi_OAouth10 extends DefaultApi10a {
	
	
	private static class InstanceHolder {
        private static final NokiaApi_OAouth10 INSTANCE = new NokiaApi_OAouth10();
    }
	
	public static NokiaApi_OAouth10 instance() {
        return InstanceHolder.INSTANCE;
    }
	
	@Override
	public OAuth1SignatureType getSignatureType() {
		return OAuth1SignatureType.QueryString;
	}


	@Override
	public String getRequestTokenEndpoint() {
		return Constant.NOKIA_HEALTH_TOKEN_REQUEST_ENDPOINT_URL;
	}

	@Override
	public String getAccessTokenEndpoint() {
		return Constant.NOKIA_HEALTH_TOKEN_ACCES_ENDPOINT_URL;
	}

	@Override
	public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
		Properties config = Utils.filesProperties(Constant.NOKIA_HEALTH_PATH_FILE_OAUTH_CONFIG);
		final ParameterList parameters = new ParameterList();
		parameters.add(OAuthConstants.TOKEN, requestToken.getToken());
		parameters.add(OAuthConstants.NONCE, getTimestampService().getNonce());
		parameters.add(OAuthConstants.SIGN_METHOD, getSignatureService().getSignatureMethod());
		parameters.add(OAuthConstants.TIMESTAMP, getTimestampService().getTimestampInSeconds());
		parameters.add(OAuthConstants.VERSION,config.getProperty(OAuthConstants.VERSION));
		parameters.add(OAuthConstants.CLIENT_ID,config.getProperty(OAuthConstants.CLIENT_ID));
		final String baseUrl = parameters.appendTo(getAuthorizationBaseUrl());
		final String signature = getSignatureService().getSignature(baseUrl, config.getProperty(OAuthConstants.CLIENT_SECRET), "");
		parameters.add(OAuthConstants.SIGNATURE,signature);
		return parameters.appendTo(getAuthorizationBaseUrl());
	}
	
	@Override
	protected String getAuthorizationBaseUrl() {
		return Constant.NOKIA_HEALTH_BASE_AUTH_URL;
	}
	
	/*@Override
	public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
		Properties config = Utils.filesProperties(NokiaApiConstant.PATH_FILE_OAUTH_CONFIG);
		final String baseString = String.format(NokiaApiConstant.AUTH_URL_FORMAT,
				config.getProperty(NokiaApiConstant.CLIENT_ID),
       			getTimestampService().getNonce(),
       			getSignatureService().getSignatureMethod(),
       			getTimestampService().getTimestampInSeconds(),
       			requestToken.getToken(),
       			config.getProperty(NokiaApiConstant.CLIENT_VERSION)
       			);
       	final String signature = getSignatureService().getSignature(baseString, config.getProperty(NokiaApiConstant.CLIENT_SECRET), "");
		return getSignedURL(baseString, signature);
	}
	
	private String getSignedURL (String baseString, String signature) {
		return String.format(baseString + NokiaApiConstant.PARAM_OAUTH_SIGNATURE,signature);
	}*/
		
}
