package outils;

public class Constant {
	
	public static final String HOST = "http://datavatar.sytes.net:8080/";
	
	/**Fitbit*/
	public static final String FITBIT_PROPS = "fitbit.properties";
	public static final String FITBIT_API_NAME = "fitbit";
	public static final String FITBIT_PROVIDER = "Fitbit";
	
	public static final String FITBIT_BASE_URL = "https://www.fitbit.com/oauth2/authorize";
	public static final String FITBIT_TOKEN_ENDPOINT_URL = "https://api.fitbit.com/oauth2/token";
	public static final String FITBIT_REVOKE_ENDPOINT_URL= "https://api.fitbit.com/oauth2/revoke";
    
    public static final String FITBIT_CALLBACK_URL = "datavatarapp://fitbit/oauth2/verification";
    
	public static final String FITBIT_PROTECTED_RESOURCE_PROFIL_URL = " https://api.fitbit.com/1/user/-/profile.json";
	
	/**Nokia Health*/
	public static final String NOKIA_HEALTH_PROPS = "nokiahealth.properties";
	public static final String NOKIA_HEALTH_API_NAME = "nokia_health";
	
	private static final String NOKIA_HEALTH_BASE_URL = "https://account.health.nokia.com";
	public static final String NOKIA_HEALTH_BASE_AUTH_URL = NOKIA_HEALTH_BASE_URL + "/oauth2_user/authorize2";
	public static final String NOKIA_HEALTH_TOKEN_ENDPOINT_URL = NOKIA_HEALTH_BASE_URL + "/oauth2/token";
	
	//public static final String NOKIA_HEALTH_TOKEN_REQUEST_ENDPOINT_URL = NOKIA_HEALTH_BASE_URL + "/request_token";
	
	
	public static final String NOKIA_HEALTH_CALLBACK_URL = "datavatarapp://nokia_health/oauth2/verification";
	
	public static final String NOKIAHEALTH_ACTIVITIES = "http://api.health.nokia.com/measure?action=getmeas";
	

	/**Garmin*/
	public static final String GARMIN_PROPS = "garmin.properties";
	
	public static final String GARMIN_API_NAME = "garmin";
	
	private static final String GARMIN_BASE_URL = "https://connectapi.garmin.com/oauth-service/oauth";
	
	public static final String GARMIN_TOKEN_ACCES_ENDPOINT_URL = GARMIN_BASE_URL+ "/access_token";   
	public static final String GARMIN_TOKEN_REQUEST_ENDPOINT_URL = GARMIN_BASE_URL + "/request_token";
	public static final String GARMIN_BASE_AUTH_URL = "https://connect.garmin.com/oauthConfirm";
	
	public static final String GARMIN_CALLBACK_URL = "datavatarapp://garmin/oauth1/verification";
	
	public static final String GARMIN_EPOCHS = "https://healthapi.garmin.com/wellness-api/rest/epochs?"
			+ "uploadStartTimeInSeconds=%s&"
			+ "uploadEndTimeInSeconds=%s";
	
	public static final String GARMIN_SLEEPS = "https://healthapi.garmin.com/wellness-api/rest/sleeps?"
			+ "uploadStartTimeInSeconds=%s&"
			+ "uploadEndTimeInSeconds=%s";

	public static final int STATUS_TOKEN_NOT_FIND = 283;

	

    

	
}
