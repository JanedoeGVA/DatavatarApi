package domaine.oauth2;

public class Oauth2Authorisation {
	
	private String api;
	private String urlVerification;
	
	//**Accesseurs*/
	public String getApi() {return this.api;}
	public String getUrlVerification() {return this.urlVerification;}
	
	//*Initialisateurs*/
	public void setApi (String api) {this.api = api;}
	public void setUrlVerification(String urlVerification) {this.urlVerification = urlVerification;}

}
