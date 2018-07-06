package main;

import java.lang.reflect.Type;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.eclipse.persistence.internal.jaxb.WrappedValue;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import domaine.oauth.OauthAccessToken;
import domaine.oauth.ProtectedListDataOauth;
import domaine.oauth.RequestProtectedData;
import domaine.oauth1a.Oauth1AccessToken;
import domaine.oauth1a.Oauth1Authorisation;
import metier.garmin.GarminPlugin;
import pojo.garmin.Epoch;
import pojo.garmin.sleep.Sleep;


@Path("/garmin")
public class Garmin {
	
	@Path("/authorisation")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Oauth1Authorisation authorisation() { 
		return GarminPlugin.getOauth1Authorisation();
	}
	
	@Path("/verification")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Oauth1AccessToken verification (
			@QueryParam ("req_token_key") String requestTokenKey,
			@QueryParam ("req_token_secret") String requestTokenSecret,
			@QueryParam ("verifier") String verifier) {
		return GarminPlugin.getAccessToken(requestTokenKey,requestTokenSecret,verifier);
	}
	
	@Path("/protecteddata/epochs")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public ProtectedListDataOauth<Epoch, Oauth1AccessToken> getEpochs (String genericJson) {
		RequestProtectedData<Oauth1AccessToken> requestProtectedData = deserializeRequestProtectedData(genericJson);
		ProtectedListDataOauth<Epoch, Oauth1AccessToken> protectedEpoch = GarminPlugin.protectedEpoch(requestProtectedData);
		return protectedEpoch;
	}
	
	
	@Path("/protecteddata/sleeps")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	//TODO trouver un moyen d'envoyer directement un RequestProtectedData<Oauth1AccessToken> à la place du String 
	public ProtectedListDataOauth<Sleep, Oauth1AccessToken> getSleeps (String genericJson) {
		RequestProtectedData<Oauth1AccessToken> requestProtectedData = deserializeRequestProtectedData(genericJson);
		ProtectedListDataOauth<Sleep, Oauth1AccessToken> protectedListDataOauth = GarminPlugin.protectedSleep(requestProtectedData);
		return protectedListDataOauth;
	}
	
	/*//TODO trouver un moyen d'envoyer directement un RequestProtectedData<Oauth1AccessToken> à la place du String 
	@Path("/protecteddata/testgeneric")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public RequestProtectedData<Oauth1AccessToken> getAGeneric (String genericJson) {
		//System.out.println("geneic entity" + genericEntity.getEntity().getOauthAccessToken().getAccessTokenKey());
		RequestProtectedData<Oauth1AccessToken> requestProtectedData = deserializeRequestProtectedData(genericJson);
		System.out.println("Cocuou");
		return requestProtectedData;
	}*/
	
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
