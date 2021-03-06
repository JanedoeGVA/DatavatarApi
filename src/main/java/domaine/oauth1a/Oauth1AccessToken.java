package domaine.oauth1a;

import domaine.authorization.Token;

public class Oauth1AccessToken extends Token {
	
	private String secret;
	
	public Oauth1AccessToken() {super();}
	
	public Oauth1AccessToken(String accessToken, String secret) {
		super(accessToken);
		this.secret = secret;
	}
	
	/**Accesseurs */
	public String getSecret() {return this.secret;}
	
	/**Initialisateurs */
	public void setSecret (String secret) {this.secret = secret;}
}
