package domaine.oauth;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import domaine.oauth1a.Oauth1AccessToken;
import domaine.oauth2.Oauth2AccessToken;

@XmlType(name="Token")
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
