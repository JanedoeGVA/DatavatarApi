package main;


import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import domaine.oauth2.Oauth2AccessToken;
import domaine.oauth2.Oauth2Authorisation;
import metier.withings.WithingsPlugin;

@Path("/withings")
public class Withings {

	@Path("/authorization")
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
	
//	@Path("/protecteddata/activitymeasures")
//	@POST
//	@Produces(MediaType.APPLICATION_JSON)
//	public ProtectedDataOauth<ActivityMeasures, Oauth2AccessToken> protectedData (Oauth2AccessToken oauth2AccessToken) {
//		return WithingsPlugin.getActivityMeasures(oauth2AccessToken);
//	}
	
	@Path("/protecteddata/hearthrate")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response protectedDataHearthRateResponse (
			@QueryParam ("date") String startDate,
			@QueryParam ("end-date") String endDate,
			@HeaderParam("assertion") String encryptToken) {
		return WithingsPlugin.getHearthRate(encryptToken,startDate,endDate); 

	}
	
	@Path("/refresh")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response refresh (@HeaderParam("assertion") String refreshToken) {
		Oauth2AccessToken oauth2AccessToken = WithingsPlugin.refresh(refreshToken);
		if (oauth2AccessToken != null) {
			return Response.status(Response.Status.OK.getStatusCode())
					.entity(oauth2AccessToken)
					.build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED.getStatusCode())
					.entity("error: token invalid")
					.build();
		}

	}
	
}
 