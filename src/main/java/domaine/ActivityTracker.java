package domaine;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import domaine.oauth.OauthAccessToken;


public class ActivityTracker {
	
	private String provider;
	private String protocol;
	@XmlElement(name="token")
	@XmlTransient
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
