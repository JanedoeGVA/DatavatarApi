package outils;

public class Constant {
	
	public static final String HOST = "http://datavatar.sytes.net:8080/";
	
	/**Fitbit*/
	public static final String FITBIT_PROPS = "fitbit.properties";
	public static final String FITBIT_PROVIDER = "Fitbit";
	
	public static final String FITBIT_BASE_URL = "https://www.fitbit.com/oauth2/authorize";
	public static final String FITBIT_TOKEN_ENDPOINT_URL = "https://api.fitbit.com/oauth2/token";
	public static final String FITBIT_REVOKE_ENDPOINT_URL= "https://api.fitbit.com/oauth2/revoke";
    
    public static final String FITBIT_CALLBACK_URL = "datavatarapp://fitbit/oauth2/verification";
    
	public static final String FITBIT_PROTECTED_RESOURCE_PROFIL_URL = " https://api.fitbit.com/1/user/-/profile.json";
	
	/**Withings*/
	public static final String WITHINGS_PROPS = "withings.properties";
	public static final String WITHINGS_PROVIDER = "Withings";
	
	private static final String WITHINGS_BASE_URL = "https://account.withings.com";
	public static final String WITHINGS_BASE_AUTH_URL = WITHINGS_BASE_URL + "/oauth2_user/authorize2";
	public static final String WITHINGS_TOKEN_ENDPOINT_URL = WITHINGS_BASE_URL + "/oauth2/token";
	
	//public static final String NOKIA_HEALTH_TOKEN_REQUEST_ENDPOINT_URL = NOKIA_HEALTH_BASE_URL + "/request_token";
	
	
	public static final String WITHINGS_CALLBACK_URL = "datavatarapp://withings/oauth2/verification";
	
	public static final String NOKIAHEALTH_ACTIVITIES = "http://api.health.nokia.com/measure?action=getmeas";
	

	/**Garmin*/
	public static final String GARMIN_PROPS = "garmin.properties";
	
	public static final String GARMIN_PROVIDER = "Garmin";
	
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
	
	/**Strava */
	public static final String STRAVA_PROPS = "strava.properties";
	public static final String STRAVA_PROVIDER = "Strava";
	
	public static final String STRAVA_BASE_URL = "https://www.strava.com/oauth/authorize";
	public static final String STRAVA_TOKEN_ENDPOINT_URL = "https://www.strava.com/oauth/token";
	//public static final String STRAVA_REVOKE_ENDPOINT_URL= "https://api.fitbit.com/oauth2/revoke";
    
    public static final String STRAVA_CALLBACK_URL = "datavatarapp://strava/oauth2/verification";
	
	
	

	

    

	
}
