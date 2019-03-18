package domaine.oauth;


public abstract class OauthAccessToken {
	
	private String accessTokenKey;
	
	public OauthAccessToken() {}
	
	public OauthAccessToken(String accessTokenKey) {
		this.accessTokenKey = accessTokenKey;
	}
	
	public String getAccessTokenKey() {return accessTokenKey;}
	
	public void setAccessTokenKey(String accessTokenKey) {this.accessTokenKey = accessTokenKey;}
}
