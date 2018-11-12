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
import metier.withings.WithingsPlugin;
import pojo.withings.ActivityMeasures;

@Path("/withings")
public class Withings {

	@Path("/authorisation")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Oauth2Authorisation authorisation() { 
		return WithingsPlugin.urlVerification();
	}
	
	@Path("/verification")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Oauth2AccessToken verification (@QueryParam ("code") String code) {
		return WithingsPlugin.accessToken(code);
		
	}
	
	@Path("/protecteddata/activitymeasures")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public ProtectedDataOauth<ActivityMeasures, Oauth2AccessToken> protectedData (Oauth2AccessToken oauth2AccessToken) {
		return WithingsPlugin.getActivityMeasures(oauth2AccessToken);
	}
	
}
 