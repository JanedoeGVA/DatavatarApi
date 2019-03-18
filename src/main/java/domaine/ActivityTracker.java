package domaine;

import javax.xml.bind.annotation.XmlElement;

import domaine.oauth.OauthAccessToken;

public class ActivityTracker {
	
	private String provider;
	private String protocol;
	@XmlElement(name="token")
	private OauthAccessToken token;
	
	public ActivityTracker() {};
	
	public ActivityTracker(String provider, String protocol, OauthAccessToken oauthAccessToken) {
		this.provider = provider;
		this.protocol = protocol;
		this.token = oauthAccessToken;
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
		return token;
	}
	public void setOauthAccessToken(OauthAccessToken oauthAccessToken) {
		this.token = oauthAccessToken;
	}
	
	

}
