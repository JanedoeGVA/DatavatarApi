package domaine.authorization;

import java.util.HashMap;
import java.util.Map;

public class RequestProtectedData<T extends Token> {
	
	private T oauthAccessToken;
	private Map<String, String> hmParams = new HashMap<String, String>();

	public T getOauthAccessToken() {return oauthAccessToken;}
	public Map<String, String> getHmParams() {return hmParams;}
	
	public void setOauthAccessToken(T oauthAccessToken) {this.oauthAccessToken = oauthAccessToken;}
	@SuppressWarnings("unchecked")
	public void setHmParams(Object object) {
		if (object instanceof Map){
			this.hmParams.putAll((Map<String, String>) object);
		}
	}
}
