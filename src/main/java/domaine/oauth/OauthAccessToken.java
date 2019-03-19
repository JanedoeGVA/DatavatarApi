package domaine.oauth;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import domaine.oauth1a.Oauth1AccessToken;
import domaine.oauth2.Oauth2AccessToken;

@XmlSeeAlso({Oauth2AccessToken.class, Oauth1AccessToken.class})
@XmlAccessorType(XmlAccessType.NONE)
public abstract class OauthAccessToken {

	private String key;

	public OauthAccessToken() {}

	public OauthAccessToken(String key) {
		this.key = key;
	}

	public String getKey() {return key;}

	public void setKey(String key) {this.key = key;}
}

