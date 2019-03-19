package domaine.oauth2;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import domaine.oauth.OauthAccessToken;

@XmlRootElement
public class Oauth2AccessToken extends OauthAccessToken {
	
	@XmlElement(name="refreshTokenKey")
	private String refreshTokenKey;
	
	public Oauth2AccessToken() {super();}
	
	public Oauth2AccessToken(String accessTokenKey, String refreshTokenKey) {
		super(accessTokenKey);
		this.refreshTokenKey = refreshTokenKey;
	}
	
	public String getRefreshTokenKey() {return refreshTokenKey;}
	
	public void setRefreshTokenKey(String key) {this.refreshTokenKey = key;}
		

}
