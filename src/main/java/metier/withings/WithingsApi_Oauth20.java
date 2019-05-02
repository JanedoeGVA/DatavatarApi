package metier.withings;

import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.oauth.OAuth20Service;
import outils.Constant;

import javax.ws.rs.RedirectionException;

import static javax.ws.rs.core.Response.Status.SEE_OTHER;

public class WithingsApi_Oauth20 extends DefaultApi20 {

    protected WithingsApi_Oauth20() {}

    private static class InstanceHolder {
        private static final WithingsApi_Oauth20 INSTANCE = new WithingsApi_Oauth20();
    }

    public static WithingsApi_Oauth20 instance() {
        return InstanceHolder.INSTANCE;
    }
    
    @Override
    public String getAccessTokenEndpoint() {
        return Constant.WITHINGS_TOKEN_ENDPOINT_URL;
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return Constant.WITHINGS_BASE_AUTH_URL;
    }
    
	@Override
	public String getRefreshTokenEndpoint() {
		return Constant.WITHINGS_TOKEN_ENDPOINT_URL;
	}

    @Override
    public String getRevokeTokenEndpoint() {
        try {
            throw new RedirectionException(SEE_OTHER, new URI(Constant.WITHINGS_URL_UI_REVOKE));
        } catch (URISyntaxException ex) {
            throw new IllegalStateException(ex);
        }
}

	@Override
	public OAuth20Service createService(String apiKey, String apiSecret, String callback, String scope,
			OutputStream debugStream, String state, String responseType, String userAgent,
			HttpClientConfig httpClientConfig, HttpClient httpClient) {
		// TODO Auto-generated method stub
		return new WithingsApi_OAuth20_ServiceImpl(this,apiKey, apiSecret, callback, scope, debugStream, state, responseType, userAgent,
				httpClientConfig, httpClient);
	}

	
    
    

    
}