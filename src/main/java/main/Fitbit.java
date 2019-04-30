package main;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import domaine.ActivityTracker;
import domaine.oauth2.Oauth2AccessToken;
import domaine.oauth2.Oauth2Authorisation;

import metier.exception.InvalidJSONException;

import metier.exception.UnAuthorizedException;
import metier.fitbit.FitbitPlugin;
import outils.Constant;
import outils.SymmetricAESKey;
import pojo.HeartRateData;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.Response.Status.*;

@Path("/fitbit")
public class Fitbit {

	private static final Logger LOG = Logger.getLogger(Fitbit.class.getName());

	@Path("/authorization")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	// TODO must return a response, code, throw error ?
	public Oauth2Authorisation authorisation() { 
		return FitbitPlugin.urlVerification();
	}

	@Path("/verification")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	// TODO throw error ?
	public Response verification (@QueryParam ("code") String code) {
		final Oauth2AccessToken oauth2AccessToken = FitbitPlugin.accessToken(code);

		final ActivityTracker activityTracker = new ActivityTracker(Constant.FITBIT_PROVIDER,Constant.TYPE_OAUTH2,oauth2AccessToken);
		return Response.status(OK)
				.entity(activityTracker)
				.build();
	}

	@Path("/refresh")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	// TODO replace return token null with try catch in plugin and thorw error
	public Response refresh (@HeaderParam("assertion") String encryptRefreshToken) {
		LOG.log(Level.INFO, "refresh :" + encryptRefreshToken);
		LOG.log(Level.INFO, "refresh decrypt:" + SymmetricAESKey.decrypt(encryptRefreshToken));
		Oauth2AccessToken oauth2AccessToken = FitbitPlugin.refresh(encryptRefreshToken);
		if (oauth2AccessToken != null) {
			// final ActivityTracker activityTracker = new ActivityTracker(Constant.FITBIT_PROVIDER, Constant.TYPE_OAUTH2 ,oauth2AccessToken);
			return Response.status(OK)
					.entity(oauth2AccessToken)
					.build();
		} else {
			return Response.status(UNAUTHORIZED)
					.entity("error: token invalid")
					.build();
		}

	}

	@Path("/revoke")
	@DELETE
	public void revoke(@HeaderParam("assertion") String token) {
		FitbitPlugin.revoke(token);
	}

	@Path("/protecteddata/heart-rate")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response protectedDataHearthRateResponse(
			@QueryParam ("date") long startDate,
			@QueryParam ("end-date") long endDate,
			@HeaderParam(AUTHORIZATION) String bearer) {
		String encryptToken = bearer.substring(bearer.lastIndexOf(" ") + 1 );
		LOG.log(Level.INFO,"decrypt token" + SymmetricAESKey.decrypt(encryptToken));
		try {
			HeartRateData heartRateData = FitbitPlugin.getHeartRate(encryptToken,startDate,endDate);
			return Response.status(OK)
					.entity(heartRateData)
					.build();

			// TODO catch all exception
		} catch (InvalidJSONException e) {
			LOG.log(Level.SEVERE, "InvalidJSONException : il y a eu un probleme avec le parsing du JSON" + e.getMessage());
			return Response.status(INTERNAL_SERVER_ERROR).build();
		} catch (UnAuthorizedException e) {
			return Response.status(UNAUTHORIZED).build();
		}  catch (IOException e) {
			LOG.log(Level.SEVERE, "IOException" + e.getMessage());
			return Response.status(INTERNAL_SERVER_ERROR).build();
		}



	}

}
