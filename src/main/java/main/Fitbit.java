package main;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.DELETE;
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
import metier.Plugin;
import metier.fitbit.FitbitPlugin;

@Path("/fitbit")
public class Fitbit {

	private static final Logger LOG = Logger.getLogger(Fitbit.class.getName());

	@Path("/authorization")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Oauth2Authorisation authorisation() { 
		return FitbitPlugin.urlVerification();
	}

	@Path("/verification")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Oauth2AccessToken verification (@QueryParam ("code") String code) {
		return FitbitPlugin.accessToken(code);

	}

	@Path("/revoke")
	@DELETE
	public void revoke(@HeaderParam("assertion") String token) {
		FitbitPlugin.revoke(token);
	}

//	@Path("/protecteddata/profil")
//	@POST
//	@Produces(MediaType.APPLICATION_JSON)
//	public ProtectedDataOauth<Profil,Oauth2AccessToken> protectedDataProfil (Oauth2AccessToken oauth2AccessToken) {
//		return FitbitPlugin.getProfil(oauth2AccessToken);
//	}

//	@Path("/protecteddata/hearthrate")
//	@POST
//	@Produces(MediaType.APPLICATION_JSON)
//	public ProtectedDataOauth<HearthRateInterval, Oauth2AccessToken> protectedDataHearthRate (@QueryParam ("date") String date,Oauth2AccessToken oauth2AccessToken) {
//		LOG.log(Level.INFO,"requesting DataHearthRate");
//		LOG.log(Level.INFO,"date :" + date);
//		LOG.log(Level.INFO, "token : " + oauth2AccessToken.toString());
//		return FitbitPlugin.getHearthRate(oauth2AccessToken,date);
//	}



	@Path("/protecteddata/hearthrate")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response protectedDataHearthRateResponse (
			@QueryParam ("date") String startDate,
			@QueryParam ("end-date") String endDate,
			@QueryParam ("detail-level") String detailLevel,
			@HeaderParam("assertion") String encryptToken) {
		Plugin.stateToken(encryptToken);
		Response response = FitbitPlugin.getHearthRate(encryptToken,startDate,endDate,detailLevel);
		return response; 

	}

	@Path("/refresh")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response refresh (@HeaderParam("assertion") String refreshToken) {
		LOG.log(Level.INFO, "refresh :" + refreshToken);
		Oauth2AccessToken oauth2AccessToken = FitbitPlugin.refresh(refreshToken);
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
