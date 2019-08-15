package metier.strava;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import outils.Constant;

import static com.github.scribejava.core.model.OAuthConstants.ACCESS_TOKEN;

public class StravaApi_OAuth20_ServiceImpl extends OAuth20Service {

	private static final Logger LOG = Logger.getLogger(StravaApi_OAuth20_ServiceImpl.class.getName());

	   public StravaApi_OAuth20_ServiceImpl(DefaultApi20 api, String apiKey, String apiSecret, String callback,
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

	@Override
	protected OAuthRequest createRevokeTokenRequest(String tokenToRevoke, TokenTypeHint tokenTypeHint) {
		final OAuthRequest request = new OAuthRequest(Verb.POST, this.getApi().getRevokeTokenEndpoint());
		request.addParameter(ACCESS_TOKEN, tokenToRevoke);
		return request;
	}

	//Just for print when revoking
	@Override
	public void revokeToken(String tokenToRevoke) throws IOException, InterruptedException, ExecutionException {
		LOG.log(Level.INFO,"Revoking token...");
		final OAuthRequest request = createRevokeTokenRequest(tokenToRevoke, null);
		checkForErrorRevokeToken(execute(request));
	}

	//Just for print when revoking
	private void checkForErrorRevokeToken(Response response) throws IOException {
		LOG.log(Level.INFO,"Body revoke token = " + response.getBody());
		LOG.log(Level.INFO,"Code revoke token = " + response.getCode());
		if (!(response.getCode() == 200 || response.getCode()== 401)) {
			OAuth2AccessTokenJsonExtractor.instance().generateError(response.getBody());
		}
	}



}
