package domaine.oauth2;

import javax.xml.bind.annotation.XmlElement;

import domaine.oauth.OauthAccessToken;

public class Oauth2AccessToken extends OauthAccessToken {
	
	@XmlElement(name="refreshTokenKey")
	private String refreshTokenKey;
	private String test = "toto";
	@XmlElement(name="test3")
	private String test2 = "tit";
	public Oauth2AccessToken() {super();}
	
	public Oauth2AccessToken(String accessTokenKey, String refreshTokenKey) {
		super(accessTokenKey);
		this.refreshTokenKey = refreshTokenKey;
	}
	
	public String getRefreshTokenKey() {return refreshTokenKey;}
	
	public void setRefreshTokenKey(String key) {this.refreshTokenKey = key;}
		

}
