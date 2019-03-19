package domaine.authorization;

public class ProtectedDataOauth<ProtectedDataT,OauthAccessTokenT extends Token> {
	
	private OauthAccessTokenT oauthAccessTokenT;
	private ProtectedDataT protectedDataT;

	public OauthAccessTokenT getOauthAccessTokenT() {return oauthAccessTokenT;}
	public ProtectedDataT getProtectedDataT() {return protectedDataT;}

	public void setOauthAccessTokenT(OauthAccessTokenT oauthAccessTokenT) {this.oauthAccessTokenT = oauthAccessTokenT;}
	public void setProtectedDataT(ProtectedDataT protectedDataT) {this.protectedDataT = protectedDataT;}	
	
}