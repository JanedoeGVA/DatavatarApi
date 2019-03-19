package domaine.oauth;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import domaine.oauth1a.Oauth1AccessToken;
import domaine.oauth2.Oauth2AccessToken;

@XmlSeeAlso({Oauth2AccessToken.class, Oauth1AccessToken.class})
@XmlTransient
public abstract class OauthAccessToken {

	@XmlElement
	private String key;

	public OauthAccessToken() {}

	public OauthAccessToken(String key) {
		this.key = key;
	}

	public String getKey() {return key;}

	public void setKey(String key) {this.key = key;}
}

