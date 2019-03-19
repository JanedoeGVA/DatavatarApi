package domaine.oauth2;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import domaine.authorization.Token;

public class Oauth2AccessToken extends Token {

	private String refreshToken;
	
	public Oauth2AccessToken() {super();}
	
	public Oauth2AccessToken(String key, String refreshToken) {
		super(key);
		this.refreshToken = refreshToken;
	}
	
	public String getRefreshToken() {return refreshToken;}
	
	public void setRefreshToken(String refreshToken) {this.refreshToken = refreshToken;}
		

}
