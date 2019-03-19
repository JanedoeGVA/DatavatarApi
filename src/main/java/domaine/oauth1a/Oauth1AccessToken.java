package domaine.oauth1a;

import javax.xml.bind.annotation.XmlRootElement;

import domaine.authorization.Token;



@XmlRootElement
public class Oauth1AccessToken extends Token {
	
	private String tokenSecret;
	
	public Oauth1AccessToken() {super();}
	
	public Oauth1AccessToken(String key, String tokenSecret) {
		super(key);
		this.tokenSecret = tokenSecret;
	}
	
	/**Accesseurs */
	public String getTokenSecret() {return this.tokenSecret;}
	
	/**Initialisateurs */
	public void setTokenSecret (String tokenSecret) {this.tokenSecret = tokenSecret;}
}
