package domaine.oauth2;

import domaine.oauth.OauthAccessToken;

public class Oauth2AccessToken extends OauthAccessToken {
	
	private String refreshTokenKey;
	
	public Oauth2AccessToken() {super();}
	
	public Oauth2AccessToken(String provider, String accessTokenKey, String refreshTokenKey,boolean isValide) {
		super(provider, accessTokenKey, isValide);
		this.refreshTokenKey = refreshTokenKey;
	}
	
	public String getRefreshTokenKey() {return refreshTokenKey;}
	
	public void setRefreshTokenKey(String key) {this.refreshTokenKey = key;}
	
	
	

}
