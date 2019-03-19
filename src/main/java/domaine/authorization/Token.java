package domaine.authorization;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import domaine.oauth1a.Oauth1AccessToken;
import domaine.oauth2.Oauth2AccessToken;

@XmlSeeAlso({Oauth2AccessToken.class, Oauth1AccessToken.class})
public abstract class Token {

	private String accessToken;

	public Token() {}

	public Token(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessToken() {return accessToken;}

	public void setAccessToken(String accessToken) {this.accessToken = accessToken;}
}

