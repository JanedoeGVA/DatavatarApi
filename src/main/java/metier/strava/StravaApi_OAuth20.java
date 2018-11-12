package metier.strava;

import java.io.OutputStream;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.oauth.OAuth20Service;

import outils.Constant;

public class StravaApi_OAuth20 extends DefaultApi20 {

    protected StravaApi_OAuth20() {}

    private static class InstanceHolder {
        private static final StravaApi_OAuth20 INSTANCE = new StravaApi_OAuth20();
    }

    public static StravaApi_OAuth20 instance() {
        return InstanceHolder.INSTANCE;
    }
    
    @Override
    public String getAccessTokenEndpoint() {
        return Constant.STRAVA_TOKEN_ENDPOINT_URL;
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return Constant.STRAVA_BASE_URL;
    }
    
	@Override
	public String getRefreshTokenEndpoint() {
		return Constant.STRAVA_TOKEN_ENDPOINT_URL;
	}
/*
	@Override
	public String getRevokeTokenEndpoint() {
		return Constant.STRAVA_REVOKE_ENDPOINT_URL;
	}
*/
	@Override
	public OAuth20Service createService(String apiKey, String apiSecret, String callback, String scope,
			OutputStream debugStream, String state, String responseType, String userAgent,
			HttpClientConfig httpClientConfig, HttpClient httpClient) {
		// TODO Auto-generated method stub
		return new StravaApi_OAuth20_ServiceImpl(this,apiKey, apiSecret, callback, scope, debugStream, state, responseType, userAgent,
				httpClientConfig, httpClient);
	}

}
