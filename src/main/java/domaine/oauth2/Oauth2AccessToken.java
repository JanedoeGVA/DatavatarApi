package domaine.oauth2;

import domaine.authorization.Token;

public class Oauth2AccessToken extends Token {

	private String refreshToken;
	
	public Oauth2AccessToken() {super();}
	
	public Oauth2AccessToken(String accessToken, String refreshToken) {
		super(accessToken);
		this.refreshToken = refreshToken;
	}
	
	public String getRefreshToken() {return refreshToken;}
	
	public void setRefreshToken(String refreshToken) {this.refreshToken = refreshToken;}
		

}
