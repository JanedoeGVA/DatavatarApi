package main;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import domaine.Activity;
import domaine.ActivityTracker;
import domaine.oauth2.Oauth2AccessToken;
import domaine.oauth2.Oauth2Authorisation;
import metier.exception.UnAuthorizedException;
import metier.strava.StravaPlugin;
import outils.Constant;
import pojo.HeartRateData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

import static javax.ws.rs.core.Response.Status.*;

@Path("/strava")
public class Strava {

	private static final Logger LOG = Logger.getLogger(Strava.class.getName());
	
	@Path("/authorization")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Oauth2Authorisation authorisation() { 
		return StravaPlugin.urlVerification();
	}


	@Path("/verification")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	// TODO throw error ?
	public Response verification (@QueryParam ("code") String code) {
		final Oauth2AccessToken oauth2AccessToken = StravaPlugin.accessToken(code);
		final ActivityTracker activityTracker = new ActivityTracker(Constant.STRAVA_PROVIDER, Constant.TYPE_OAUTH2,oauth2AccessToken);
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
		Oauth2AccessToken oauth2AccessToken = StravaPlugin.refresh(encryptRefreshToken);
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

	@Path("/revoke-method")
	@GET
	public Response getRevokeMethod() {
		LOG.log(Level.INFO,"revoke-method called");
		final String json = "{\"method\":\"post\"}";
		return Response.status(OK).entity(json).build();
	}

	@Path("/revoke")
	@POST
	public Response revoke(@HeaderParam(AUTHORIZATION) String bearer) {
		String encryptToken = bearer.substring(bearer.lastIndexOf(" ") + 1 );
		LOG.log(Level.INFO,"crypted token" + encryptToken);
		try {
			// Could return code SEE_OTHER (302) if revoke not implemented
			StravaPlugin.revoke(encryptToken);
			return Response.status(OK).build();
		} catch (IOException | InterruptedException | ExecutionException e) {
			LOG.log(Level.SEVERE,"Error during revoking token : " , e);
			return Response.serverError().build();
		} catch (Exception ex) {
			LOG.log(Level.SEVERE, "error Exception ", ex);
			return Response.status(500).build();
		}
	}


	@Path("/protecteddata/heart-rate")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response protectedDataHearthRateResponse(
			@QueryParam ("date") long startDate,
			@QueryParam ("end-date") long endDate,
			@HeaderParam(HttpHeaders.AUTHORIZATION) String bearer) {
		String encryptToken = bearer.substring(bearer.lastIndexOf(" ") + 1 );
		try {
			final ArrayList<Activity> lstActivity = StravaPlugin.ListActivitiesId(startDate,endDate,encryptToken);
			final HeartRateData heartRateData = StravaPlugin.getHeartRate(lstActivity,encryptToken);
			return Response.status(OK)
					.entity(heartRateData)
					.build();
		} catch (UnAuthorizedException e) {
			return Response.status(INTERNAL_SERVER_ERROR)
					.build();
		} catch (IOException e) {
			return Response.status(INTERNAL_SERVER_ERROR)
					.build();
		}

	}





}
