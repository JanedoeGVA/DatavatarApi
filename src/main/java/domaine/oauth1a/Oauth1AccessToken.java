package domaine.oauth1a;

import domaine.oauth.OauthAccessToken;

public class Oauth1AccessToken extends OauthAccessToken {
	
	private String accessTokenSecret;
	
	public Oauth1AccessToken() {super();}
	
	public Oauth1AccessToken(String accessTokenKey, String accessTokenSecret) {
		super(accessTokenKey);
		this.accessTokenSecret = accessTokenSecret;
	}
	
	/**Accesseurs */
	public String getAccessTokenSecret() {return this.accessTokenSecret;}
	
	/**Initialisateurs */
	public void setAccessTokenSecret (String secret) {this.accessTokenSecret = secret;}
}
