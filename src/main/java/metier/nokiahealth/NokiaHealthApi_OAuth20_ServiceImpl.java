package metier.nokiahealth;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.revoke.TokenTypeHint;

public class NokiaHealthApi_OAuth20_ServiceImpl extends OAuth20Service {

	   public NokiaHealthApi_OAuth20_ServiceImpl(DefaultApi20 api, String apiKey, String apiSecret, String callback,
				String scope, OutputStream debugStream, String state, String responseType, String userAgent,
				HttpClientConfig httpClientConfig, HttpClient httpClient) {
			super(api, apiKey, apiSecret, callback, scope, debugStream, state, responseType, userAgent, httpClientConfig,
					httpClient);
		}
	   

	@Override
	  	protected OAuthRequest createAccessTokenRequest(String code) {
		   final OAuthRequest request = new OAuthRequest(this.getApi().getAccessTokenVerb(),this.getApi().getAccessTokenEndpoint());
		   this.getApi().getClientAuthenticationType().addClientAuthentication(request, getApiKey(), getApiSecret());
	       request.addParameter(OAuthConstants.CLIENT_ID, getApiKey());
	       request.addParameter(OAuthConstants.CLIENT_SECRET, getApiSecret());
	       request.addParameter(OAuthConstants.CODE, code);
	       request.addParameter(OAuthConstants.REDIRECT_URI, getCallback());
	       request.addParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.AUTHORIZATION_CODE);
	       return request;
	  	}
	   
	    @Override
	    protected OAuthRequest createRefreshTokenRequest(String refreshToken) {
		    	if (refreshToken == null || refreshToken.isEmpty()) {
		    		throw new IllegalArgumentException("The refreshToken cannot be null or empty");
		    	}
	        final OAuthRequest request = new OAuthRequest(Verb.POST, this.getApi().getRefreshTokenEndpoint());
	        this.getApi().getClientAuthenticationType().addClientAuthentication(request, getApiKey(), getApiSecret());
	        request.addParameter(OAuthConstants.CLIENT_ID, getApiKey());
		    request.addParameter(OAuthConstants.CLIENT_SECRET, getApiSecret());
	        request.addParameter(OAuthConstants.REFRESH_TOKEN, refreshToken);
	        request.addParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.REFRESH_TOKEN);
	        return request;
	    }
	    
	



		

		

	}