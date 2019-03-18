package domaine.oauth;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import domaine.oauth1a.Oauth1AccessToken;
import domaine.oauth2.Oauth2AccessToken;

@XmlRootElement(name = "Token")
@XmlSeeAlso({Oauth2AccessToken.class, Oauth1AccessToken.class})
public abstract class OauthAccessToken {
	
	private String accessTokenKey;
	
	public OauthAccessToken() {}
	
	public OauthAccessToken(String accessTokenKey) {
		this.accessTokenKey = accessTokenKey;
	}
	
	public String getAccessTokenKey() {return accessTokenKey;}
	
	public void setAccessTokenKey(String accessTokenKey) {this.accessTokenKey = accessTokenKey;}
}
