package metier.fitbit;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.revoke.TokenTypeHint;
import com.github.scribejava.core.java8.Base64;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;

public class FitbitApi_OAuth20_ServiceImpl extends OAuth20Service {

   public FitbitApi_OAuth20_ServiceImpl(DefaultApi20 api, String apiKey, String apiSecret, String callback,
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
       final String scope = getScope();
       if(scope != null) {
           request.addParameter(OAuthConstants.SCOPE, scope);
       }
       //this is non-OAuth2 standard, but Fitbit requires it
       request.addHeader(OAuthConstants.HEADER, OAuthConstants.BASIC + " " + getKeyBytesForFitbitAuth());;
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
        request.addParameter(OAuthConstants.REFRESH_TOKEN, refreshToken);
        request.addParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.REFRESH_TOKEN);
        request.addParameter(OAuthConstants.REFRESH_TOKEN, refreshToken);
        request.addParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.REFRESH_TOKEN);
        //this is non-OAuth2 standard, but Fitbit requires it
        request.addHeader(OAuthConstants.HEADER, OAuthConstants.BASIC + " " + getKeyBytesForFitbitAuth());
        return request;
    }
    
	@Override
    protected OAuthRequest createRevokeTokenRequest(String tokenToRevoke, TokenTypeHint tokenTypeHint) {
    	final OAuthRequest request = new OAuthRequest(Verb.POST, this.getApi().getRevokeTokenEndpoint());
    	this.getApi().getClientAuthenticationType().addClientAuthentication(request, getApiKey(), getApiSecret());
        request.addParameter("token", tokenToRevoke);
        //this is non-OAuth2 standard, but Fitbit requires it
        request.addHeader(OAuthConstants.HEADER, OAuthConstants.BASIC + " " + getKeyBytesForFitbitAuth());
        System.out.println("@createRevokeTokenRequest to string " + request.toString());
       
        return request;
    }

	//Just for print when revoking
	@Override
	public void revokeToken(String tokenToRevoke) throws IOException, InterruptedException, ExecutionException {
		System.out.println("Revoking token..." );
		final OAuthRequest request = createRevokeTokenRequest(tokenToRevoke, null);
        checkForErrorRevokeToken(execute(request));
	}

	//Just for print when revoking
	private void checkForErrorRevokeToken(Response response) throws IOException {
		System.out.println("Body revoke token = " + response.getBody());
		System.out.println("Code revoke token = " + response.getCode());
        if (response.getCode() != 200) {
            OAuth2AccessTokenJsonExtractor.instance().generateError(response.getBody());
        }
    }

	

	private String getKeyBytesForFitbitAuth() {
    	String keyAndSecret = String.format("%s:%s", new Object[] {getApiKey(), getApiSecret()});
    	byte[] keyBytes = Base64.getEncoder().encode(keyAndSecret.getBytes(Charset.forName("UTF-8")));
    	return new String(keyBytes);
	}

}
