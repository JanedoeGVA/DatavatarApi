package main;


import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;


import javax.ws.rs.*;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import domaine.ActivityTracker;
import domaine.authorization.ProtectedListDataOauth;
import domaine.authorization.RequestProtectedData;
import domaine.oauth1a.Oauth1AccessToken;
import domaine.oauth1a.Oauth1Authorisation;
import metier.garmin.GarminPlugin;

import org.json.JSONObject;
import outils.Constant;
import outils.SymmetricAESKey;
import pojo.garmin.Epoch;

import static javax.ws.rs.core.Response.Status.OK;


@Path("/garmin")
public class Garmin {

	private static final Logger LOG = Logger.getLogger(Garmin.class.getName());
	
	@Path("/authorization")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Oauth1Authorisation authorisation() { 
		return GarminPlugin.getOauth1Authorisation();
	}
	
	@Path("/verification")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response verification (
			@QueryParam ("req_token_key") String requestTokenKey,
			@QueryParam ("req_token_secret") String requestTokenSecret,
			@QueryParam ("verifier") String verifier) {
		final Oauth1AccessToken oauth1AccessToken = GarminPlugin.getAccessToken(requestTokenKey,requestTokenSecret,verifier);
		final ActivityTracker activityTracker = new ActivityTracker(Constant.GARMIN_PROVIDER,Constant.TYPE_OAUTH1,oauth1AccessToken);
		return Response.status(OK)
				.entity(activityTracker)
				.build();
	}


	@Path("/protecteddata/heart-rate")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public ProtectedListDataOauth<Epoch, Oauth1AccessToken> getEpochs (@QueryParam ("date") long startDate,
																	   @QueryParam ("end-date") long endDate,
																	   @HeaderParam(HttpHeaders.AUTHORIZATION) String bearer)
	{
		String encryptToken = bearer.substring(bearer.lastIndexOf(" ") + 1 );
		String decodeToken = new String(Base64.getDecoder().decode(encryptToken), StandardCharsets.UTF_8);
		LOG.log(Level.INFO,"decodeToken" + decodeToken);
		JSONObject jsonObject = new JSONObject(decodeToken);
		Oauth1AccessToken oauth1AccessToken = new Oauth1AccessToken(SymmetricAESKey.decrypt(jsonObject.getString("accessToken")),SymmetricAESKey.decrypt(jsonObject.getString("secret")));
		LOG.log(Level.INFO,"token " + oauth1AccessToken.getAccessToken());
		LOG.log(Level.INFO,"secret " + oauth1AccessToken.getSecret());
		try {
			GarminPlugin.protectedEpoch(startDate,endDate,oauth1AccessToken);
		} catch (Exception e) {
			LOG.log(Level.WARNING,"Exception :" + e.getMessage());
		}
		return null;
	}

//	public Response protectedDataHearthRateResponse(
//			@QueryParam ("date") long startDate,
//			@QueryParam ("end-date") long endDate,
//			@HeaderParam("assertion") String encryptToken) {
//		try {
//			HeartRateData heartRateData = FitbitPlugin.getHearthRate(encryptToken,startDate,endDate);
//			return Response.status(OK)
//					.entity(heartRateData)
//					.build();
//
//			// TODO catch all exception
//		} catch (InvalidJSONException e) {
//			LOG.log(Level.SEVERE, "InvalidJSONException : il y a eu un probleme avec le parsing du JSON" + e.getMessage());
//			return Response.status(INTERNAL_SERVER_ERROR).build();
//		} catch (UnAuthorizedException e) {
//			return Response.status(UNAUTHORIZED).build();
//		}  catch (IOException e) {
//			LOG.log(Level.SEVERE, "IOException" + e.getMessage());
//			return Response.status(INTERNAL_SERVER_ERROR).build();
//		}



	// }

//	@Path("/protecteddata/epochs")
//	@POST
//	@Produces(MediaType.APPLICATION_JSON)
//	public ProtectedListDataOauth<Epoch, Oauth1AccessToken> getEpochs (String genericJson) {
//		RequestProtectedData<Oauth1AccessToken> requestProtectedData = deserializeRequestProtectedData(genericJson);
//		ProtectedListDataOauth<Epoch, Oauth1AccessToken> protectedEpoch = GarminPlugin.protectedEpoch(requestProtectedData);
//		return protectedEpoch;
//	}
	
	
//	@Path("/protecteddata/sleeps")
//	@POST
//	@Produces(MediaType.APPLICATION_JSON)
//	//TODO trouver un moyen d'envoyer directement un RequestProtectedData<Oauth1AccessToken> � la place du String
//	public ProtectedListDataOauth<Sleep, Oauth1AccessToken> getSleeps (String genericJson) {
//		RequestProtectedData<Oauth1AccessToken> requestProtectedData = deserializeRequestProtectedData(genericJson);
//		ProtectedListDataOauth<Sleep, Oauth1AccessToken> protectedListDataOauth = GarminPlugin.protectedSleep(requestProtectedData);
//		return protectedListDataOauth;
//	}
	
	@Path("/revoke")
	@DELETE
	public void revoke(Oauth1AccessToken oauth1AccessToken ) {
		GarminPlugin.revoke(oauth1AccessToken);
	}
	
	private RequestProtectedData<Oauth1AccessToken> deserializeRequestProtectedData(String genericJson) {
		Type type = new TypeToken<RequestProtectedData<Oauth1AccessToken>>() {}.getType();
		Gson gson = new Gson();  
		RequestProtectedData<Oauth1AccessToken> requestProtectedData = gson.fromJson(genericJson, type);
		return requestProtectedData;
	}
	
	/*
	@Path("/protecteddata/epochs")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Epoch> getEpochs (Oauth1AccessToken oauth1AccessToken,
			@DefaultValue("1523795630")@QueryParam ("start_time") long startTime, 
			@DefaultValue("1523882030")@QueryParam ("end_time") long endTime) {
		ArrayList<Epoch> lstEpoch = GarminPlugin.listEpoch(oauth1AccessToken,startTime,endTime);
		return lstEpoch;
	}*/
	
	/*
	@Path("/data/hmepochs")
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Epoch> getEpochs (MultivaluedMap<String,String> mData) {
		String key = mData.get("access_token_key").get(0);
		String secret = mData.get("access_token_secret").get(0);
		long startTime = Long.valueOf(mData.get("start_time").get(0));
		long endTime = Long.valueOf(mData.get("end_time").get(0));
		ArrayList<Epoch> lstEpoch = GarminPlugin.listEpoch(key,secret,startTime,endTime);
		return lstEpoch;
	}*/

}
