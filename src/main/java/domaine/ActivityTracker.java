package domaine;

import domaine.authorization.Token;

public class ActivityTracker {

	private String provider;
	private Token token;

	public ActivityTracker() {};

	public ActivityTracker(String provider, Token token) {
		this.provider = provider;
		this.token = token;
	}

	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}

	public Token getToken() {
		return token;
	}
	public void setToken(Token token) {
		this.token = token;
	}



}
