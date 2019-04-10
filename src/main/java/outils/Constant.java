package outils;

public class Constant {
	
	public static final String TYPE_OAUTH1 = "oauth1";
	public static final String TYPE_OAUTH2 = "oauth2";
	
	public static final String JSON_MEDIA_TYPE = "json";
	public static final String TOKEN = "token";
	public static final String EXPIRES_IN = "expires_in";
	public static final String TOKEN_TYPE_BEARER = "Bearer";
	
	public static final String ONE_HOUR = "3600";
	
	public static final String HOST = "http://datavatar.sytes.net:8080/";
	
	/**Fitbit*/
	public static final String FITBIT_PROPS = "fitbit.properties";
	public static final String FITBIT_PROVIDER = "Fitbit";
	
	public static final String FITBIT_BASE_URL = "https://www.fitbit.com/oauth2/authorize";
	public static final String FITBIT_TOKEN_ENDPOINT_URL = "https://api.fitbit.com/oauth2/token";
	public static final String FITBIT_REVOKE_ENDPOINT_URL= "https://api.fitbit.com/oauth2/revoke";
    
    public static final String FITBIT_CALLBACK_URL = "datavatarapp://fitbit/oauth2/verification";
    
	public static final String FITBIT_PROTECTED_RESOURCE_PROFIL_URL = " https://api.fitbit.com/1/user/-/profile.json";

	public static final String FITBIT_TEMPLATE_HEART_RATE_URL = "https://api.fitbit.com/1/user/-/activities/heart/date/{date}/{end-date}/{detail-lvl}.json";
	public static final String FITBIT_DETAIL_LVL_MIN = "1min";
	public static final String FITBIT_PARAM_DATE = "date";
	public static final String FITBIT_PARAM_END_DATE = "end-date";
	public static final String FITBIT_PARAM_DETAIL_LVL = "detail-lvl";

	/**Withings*/
	public static final String WITHINGS_PROPS = "withings.properties";
	public static final String WITHINGS_PROVIDER = "Withings";
	
	private static final String WITHINGS_BASE_URL = "https://account.withings.com";
	public static final String WITHINGS_BASE_AUTH_URL = WITHINGS_BASE_URL + "/oauth2_user/authorize2";
	public static final String WITHINGS_TOKEN_ENDPOINT_URL = WITHINGS_BASE_URL + "/oauth2/token";
	public static final String WITHINGS_MEASURE_URL= "https://wbsapi.withings.net/measure";

	public static final String WITHINGS_PARAM_END_DATE = "enddate";
	public static final String WITHINGS_PARAM_START_DATE = "startdate";
	public static final String WITHINGS_PARAM_ACTION = "action";
	public static final String WITHINGS_PARAM_MEASTYPE = "meastype";


	public static final String WITHINGS_PARAM_ACTION_GETMEAS = "getmeas";
	public static final String WITHINGS_PARAM_MEASTYPE_HR = "11";

	public static final String WITHINGS_STATUS_CODE = "status";

	//public static final String NOKIA_HEALTH_TOKEN_REQUEST_ENDPOINT_URL = NOKIA_HEALTH_BASE_URL + "/request_token";
	
	
	public static final String WITHINGS_CALLBACK_URL = "datavatarapp://withings/oauth2/verification";

	

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
