package domaine.oauth;


public abstract class OauthAccessToken {
	
	private String provider;
	private String accessTokenKey;
	
	public OauthAccessToken() {}
	
	public OauthAccessToken(String provider, String accessTokenKey) {
		this.provider = provider;
		this.accessTokenKey = accessTokenKey;
	}
	
	public String getProvider() {return provider;}
	public String getAccessTokenKey() {return accessTokenKey;}
	
	public void setProvider(String provider) {this.provider = provider;}
	public void setAccessTokenKey(String accessTokenKey) {this.accessTokenKey = accessTokenKey;}
}
