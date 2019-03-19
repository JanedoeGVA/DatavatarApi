package domaine;

import javax.xml.bind.annotation.XmlElement;

import domaine.authorization.Token;


public class ActivityTracker {
	
	private String provider;
	private String protocol;
	private Token token;
	
	public ActivityTracker() {};
	
	public ActivityTracker(String provider, String protocol, Token token) {
		this.provider = provider;
		this.protocol = protocol;
		this.token = token;
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
	public Token getOauthAccessToken() {
		return token;
	}
	public void setOauthAccessToken(Token oauthAccessToken) {
		this.token = oauthAccessToken;
	}
	
	

}
