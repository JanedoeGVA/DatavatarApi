package main;


import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import domaine.oauth.ProtectedDataOauth;
import domaine.oauth1a.Oauth1AccessToken;
import domaine.oauth2.Oauth2AccessToken;
import domaine.oauth2.Oauth2Authorisation;
import metier.fitbit.FitbitPlugin;
import metier.nokiahealth.NokiaPlugin;
import pojo.nokiahealth.ActivityMeasures;

@Path("/nokia_health")
public class NokiaHealth {

	@Path("/authorisation")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Oauth2Authorisation authorisation() { 
		return NokiaPlugin.urlVerification();
	}
	
	@Path("/verification")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Oauth2AccessToken verification (@QueryParam ("code") String code) {
		return NokiaPlugin.accessToken(code);
		
	}
	
	@Path("/protecteddata/activitymeasures")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public ProtectedDataOauth<ActivityMeasures, Oauth2AccessToken> protectedData (Oauth2AccessToken oauth2AccessToken) {
		return NokiaPlugin.getActivityMeasures(oauth2AccessToken);
	}
	
}
 