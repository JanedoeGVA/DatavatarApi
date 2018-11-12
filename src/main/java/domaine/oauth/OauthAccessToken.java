package domaine.oauth;


public abstract class OauthAccessToken {
	
	private String provider;
	private String accessTokenKey;
	private boolean isValide;
	
	public OauthAccessToken() {}
	
	public OauthAccessToken(String provider, String accessTokenKey, boolean isValide) {
		this.provider = provider;
		this.accessTokenKey = accessTokenKey;
		this.isValide = isValide;
	}
	
	public String getProvider() {return provider;}
	public String getAccessTokenKey() {return accessTokenKey;}
	public boolean getIsValide() {return isValide;}
	
	public void setProvider(String provider) {this.provider = provider;}
	public void setAccessTokenKey(String accessTokenKey) {this.accessTokenKey = accessTokenKey;}
	public void setIsValide(boolean isValide) {this.isValide = isValide;}
}
