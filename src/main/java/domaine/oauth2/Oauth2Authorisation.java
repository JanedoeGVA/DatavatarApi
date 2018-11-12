package domaine.oauth2;

public class Oauth2Authorisation {
	
	private String provider;
	private String urlVerification;
	
	//**Accesseurs*/
	public String getProvider() {return this.provider;}
	public String getUrlVerification() {return this.urlVerification;}
	
	//*Initialisateurs*/
	public void setProvider (String provider) {this.provider = provider;}
	public void setUrlVerification(String urlVerification) {this.urlVerification = urlVerification;}

}
