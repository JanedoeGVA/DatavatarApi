package metier.fitbit;

import java.awt.List;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.revoke.TokenTypeHint;

import metier.Plugin;
import metier.exception.UnAuthorizedException;
import outils.Constant;

import com.github.scribejava.core.java8.Base64;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Parameter;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;

public class FitbitApi_OAuth20_ServiceImpl extends OAuth20Service {

	private static final Logger LOG = Logger.getLogger(FitbitApi_OAuth20_ServiceImpl.class.getName());

	public FitbitApi_OAuth20_ServiceImpl(DefaultApi20 api, String apiKey, String apiSecret, String callback,
			String scope, OutputStream debugStream, String state, String responseType, String userAgent,
			HttpClientConfig httpClientConfig, HttpClient httpClient) {
		super(api, apiKey, apiSecret, callback, scope, debugStream, state, responseType, userAgent, httpClientConfig,
				httpClient);
	}

	@Override
	protected OAuthRequest createAccessTokenRequest(String code) {
		final OAuthRequest request = Plugin.createOAuth20Request(code,this);
		request.addParameter(Constant.EXPIRES_IN,"3600");
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
		request.addParameter(Constant.EXPIRES_IN,"3600");
		//this is non-OAuth2 standard, but Fitbit requires it
		request.addHeader(OAuthConstants.HEADER, OAuthConstants.BASIC + " " + getKeyBytesForFitbitAuth());
		// LOG //
		final ArrayList<Parameter> lstParam = (ArrayList<Parameter>)request.getBodyParams().getParams();
		for (Parameter parameter : lstParam) {
			LOG.log(Level.INFO, String.format("refresh request param : key %s value %s", parameter.getKey(),parameter.getValue()));
		}
		// LOG
		return request;
	}

	@Override
	protected OAuthRequest createRevokeTokenRequest(String tokenToRevoke, TokenTypeHint tokenTypeHint) {
		final OAuthRequest request = new OAuthRequest(Verb.POST, this.getApi().getRevokeTokenEndpoint());
		this.getApi().getClientAuthenticationType().addClientAuthentication(request, getApiKey(), getApiSecret());
		request.addParameter(Constant.TOKEN, tokenToRevoke);
		//this is non-OAuth2 standard, but Fitbit requires it
		request.addHeader(OAuthConstants.HEADER, OAuthConstants.BASIC + " " + getKeyBytesForFitbitAuth());
		LOG.log(Level.INFO,"@createRevokeTokenRequest to string " + request.toString());
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

	private String getKeyBytesForFitbitAuth() {
		String keyAndSecret = String.format("%s:%s", new Object[] {getApiKey(), getApiSecret()});
		byte[] keyBytes = Base64.getEncoder().encode(keyAndSecret.getBytes(Charset.forName("UTF-8")));
		return new String(keyBytes);
	}

}
