package domaine;

import domaine.oauth.OauthAccessToken;

public class ActivityTracker {
	
	private String provider;
	private String protocol;
	
	public ActivityTracker(String provider, String protocol, OauthAccessToken oauthAccessToken) {
		super();
		this.provider = provider;
		this.protocol = protocol;
		this.oauthAccessToken = oauthAccessToken;
	}
	
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public OauthAccessToken getOauthAccessToken() {
		return oauthAccessToken;
	}
	public void setOauthAccessToken(OauthAccessToken oauthAccessToken) {
		this.oauthAccessToken = oauthAccessToken;
	}
	private OauthAccessToken oauthAccessToken;
	

}
