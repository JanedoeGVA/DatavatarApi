package domaine;

import domaine.authorization.Token;

public class ActivityTracker {

	private String provider;
	private String protocol;
	private Token token;

	// public ActivityTracker() {};

	public ActivityTracker(String provider, String protocol,Token token) {
		this.provider = provider;
		this.token = token;
		this.protocol = protocol;
	}

	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	
	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public Token getToken() {
		return token;
	}
	public void setToken(Token token) {
		this.token = token;
	}



}
