package main;


import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import domaine.oauth.ProtectedDataOauth;
import domaine.oauth1a.Oauth1AccessToken;
import domaine.oauth1a.Oauth1Authorisation;
import metier.nokiahealth.NokiaPlugin;
import pojo.nokiahealth.ActivityMeasures;

@Path("/nokia_health")
public class NokiaHealth {

	@Path("/authorisation")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Oauth1Authorisation authorisation() { 
		return NokiaPlugin.getOauth1Authorisation();
	}
	
	@Path("/verification")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Oauth1AccessToken verification (
			@QueryParam ("req_token_key") String requestTokenKey,
			@QueryParam ("req_token_secret") String requestTokenSecret,
			@QueryParam ("verifier") String verifier) {
		return NokiaPlugin.getAccessToken(requestTokenKey,requestTokenSecret,verifier);
	}
	
	@Path("/protecteddata/activitymeasures")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public ProtectedDataOauth<ActivityMeasures, Oauth1AccessToken> protectedData (Oauth1AccessToken oauth1AccessToken) {
		return NokiaPlugin.getActivityMeasures(oauth1AccessToken);
	} 
	
}
 