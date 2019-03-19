package domaine.authorization;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import domaine.oauth1a.Oauth1AccessToken;
import domaine.oauth2.Oauth2AccessToken;

@XmlSeeAlso({Oauth2AccessToken.class, Oauth1AccessToken.class})
public abstract class Token {

	private String key;

	public Token() {}

	public Token(String key) {
		this.key = key;
	}

	public String getKey() {return key;}

	public void setKey(String key) {this.key = key;}
}

