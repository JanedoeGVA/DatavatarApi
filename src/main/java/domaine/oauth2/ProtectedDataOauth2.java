package domaine.oauth2;

public class ProtectedDataOauth2<T> {
	
	private Oauth2AccessToken oauth2AccessToken;
	private T protectedData;
	
	public Oauth2AccessToken getOauth2AccessToken() {return oauth2AccessToken;}
	public T getProtectedData() {return protectedData;}
	
	public void setOauth2AccessToken(Oauth2AccessToken oauth2AccessToken) {this.oauth2AccessToken = oauth2AccessToken;}
	public void setProtectedData(T protectedData) {this.protectedData = protectedData;}
	
}
