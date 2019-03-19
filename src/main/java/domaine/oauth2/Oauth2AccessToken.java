package domaine.oauth2;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import domaine.oauth.OauthAccessToken;

@XmlRootElement
@XmlType(propOrder={"key", "refreshToken"})
public class Oauth2AccessToken extends OauthAccessToken {

	private String refreshTokenKey;
	
	public Oauth2AccessToken() {super();}
	
	public Oauth2AccessToken(String key, String refreshTokenKey) {
		super(key);
		this.refreshTokenKey = refreshTokenKey;
	}
	
	public String getRefreshTokenKey() {return refreshTokenKey;}
	
	public void setRefreshTokenKey(String key) {this.refreshTokenKey = key;}
		

}
