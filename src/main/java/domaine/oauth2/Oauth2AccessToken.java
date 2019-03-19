package domaine.oauth2;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import domaine.authorization.Token;

@XmlRootElement
public class Oauth2AccessToken extends Token {

	private String refreshTokenKey;
	
	public Oauth2AccessToken() {super();}
	
	public Oauth2AccessToken(String key, String refreshTokenKey) {
		super(key);
		this.refreshTokenKey = refreshTokenKey;
	}
	
	public String getRefreshTokenKey() {return refreshTokenKey;}
	
	public void setRefreshTokenKey(String key) {this.refreshTokenKey = key;}
		

}
