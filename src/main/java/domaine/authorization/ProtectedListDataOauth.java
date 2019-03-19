package domaine.authorization;

import java.util.ArrayList;

public class ProtectedListDataOauth <ProtectedDataT,T extends Token> {
	
	private T oauthAccessToken;
	private ArrayList<ProtectedDataT> lstProtectedDataT = null;

	public T getOauthAccessToken() {return oauthAccessToken;}
	public ArrayList<ProtectedDataT> getLstProtectedDataT() {return lstProtectedDataT;}

	public void setOauthAccessToken(T oauthAccessToken) {this.oauthAccessToken = oauthAccessToken;}
	public void setLstProtectedDataT(ArrayList<ProtectedDataT> lstProtectedDataT) {this.lstProtectedDataT = lstProtectedDataT;}	

	
}