package domaine.oauth1a;

public class Oauth1Authorisation {
	
	private String provider;
	private String requestTokenKey;
	private String requestTokenSecret;
	private String verifier;
	private String urlVerification;
	
	//**Accesseurs*/
	public String getProvider() {return this.provider;}
	public String getRequestTokenKey() {return this.requestTokenKey;}
	public String getRequestTokenSecret() {return this.requestTokenSecret;}
	public String getVerifier() {return this.verifier;}
	public String getUrlVerification() {return this.urlVerification;}

	//**Initialisateurs*/
	public void setProvider (String provider) {this.provider = provider;}
	public void setRequestTokenKey (String requestTokenKey) {this.requestTokenKey = requestTokenKey;}
	public void setRequestTokenSecret (String requestTokenSecret) {this.requestTokenSecret = requestTokenSecret;}
	public void setVerifier (String verifier) {this.verifier = verifier;}
	public void setUrlVerification (String url) {this.urlVerification = url;}
}
