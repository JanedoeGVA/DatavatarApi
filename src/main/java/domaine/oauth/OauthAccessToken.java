package domaine.oauth;


public abstract class OauthAccessToken {
	
	private String apiName;
	private String accessTokenKey;
	private boolean isValide;
	
	public OauthAccessToken() {}
	
	public OauthAccessToken(String apiName, String accessTokenKey, boolean isValide) {
		this.apiName = apiName;
		this.accessTokenKey = accessTokenKey;
		this.isValide = isValide;
	}
	
	public String getApiName() {return apiName;}
	public String getAccessTokenKey() {return accessTokenKey;}
	public boolean getIsValide() {return isValide;}
	
	public void setApiName(String apiName) {this.apiName = apiName;}
	public void setAccessTokenKey(String accessTokenKey) {this.accessTokenKey = accessTokenKey;}
	public void setIsValide(boolean isValide) {this.isValide = isValide;}
}
