package domaine.oauth;

public class ProtectedDataOauth<ProtectedDataT,OauthAccessTokenT extends OauthAccessToken> {
	
	private OauthAccessTokenT oauthAccessTokenT;
	private ProtectedDataT protectedDataT;

	public OauthAccessTokenT getOauthAccessTokenT() {return oauthAccessTokenT;}
	public ProtectedDataT getProtectedDataT() {return protectedDataT;}

	public void setOauthAccessTokenT(OauthAccessTokenT oauthAccessTokenT) {this.oauthAccessTokenT = oauthAccessTokenT;}
	public void setProtectedDataT(ProtectedDataT protectedDataT) {this.protectedDataT = protectedDataT;}	
	
}