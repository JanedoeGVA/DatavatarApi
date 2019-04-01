package main;


import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import domaine.ActivityTracker;
import domaine.oauth2.Oauth2AccessToken;
import domaine.oauth2.Oauth2Authorisation;
import metier.Plugin;
import metier.fitbit.FitbitPlugin;
import metier.withings.WithingsPlugin;
import outils.Constant;

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
	public Response verification (@QueryParam ("code") String code) {
		final Oauth2AccessToken oauth2AccessToken = WithingsPlugin.accessToken(code);
		final ActivityTracker activityTracker = new ActivityTracker(Constant.WITHINGS_PROVIDER,Constant.TYPE_OAUTH2,oauth2AccessToken);
		return Response.status(Response.Status.OK.getStatusCode())
				.entity(activityTracker)
				.build();
	}


	@Path("/protecteddata/hearthrate")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response protectedDataHearthRateResponse (
			@QueryParam ("date") String startDate,
			@QueryParam ("end-date") String endDate,
			@HeaderParam("assertion") String encryptToken) {
		Response response = WithingsPlugin.getHearthRate(encryptToken,startDate,endDate); 
		return response;
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
