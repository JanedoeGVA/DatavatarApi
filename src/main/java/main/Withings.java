package main;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

import domaine.ActivityTracker;
import domaine.oauth2.Oauth2AccessToken;
import domaine.oauth2.Oauth2Authorisation;
import metier.exception.UnAuthorizedException;

import metier.withings.WithingsPlugin;
import outils.Constant;

import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.Status.*;

@Path("/withings")
public class Withings {

	private static final Logger LOG = Logger.getLogger(Withings.class.getName());

	@Path("/authorization")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	// TODO must return a response, code, error ?
	public Oauth2Authorisation authorisation() { 
		return WithingsPlugin.urlVerification();
	}

	@Path("/verification")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response verification (@QueryParam ("code") String code) {
		final Oauth2AccessToken oauth2AccessToken = WithingsPlugin.accessToken(code);
		final ActivityTracker activityTracker = new ActivityTracker(Constant.WITHINGS_PROVIDER,Constant.TYPE_OAUTH2,oauth2AccessToken);
		return Response.status(OK)
				.entity(activityTracker)
				.build();
	}

	@Path("/refresh")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response refresh (@HeaderParam("assertion") String refreshToken) {
		Oauth2AccessToken oauth2AccessToken = WithingsPlugin.refresh(refreshToken);
		if (oauth2AccessToken != null) {
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
	public Response revoke(@HeaderParam("assertion") String token) {
		return Response.status(NOT_IMPLEMENTED)
				.entity("https://account.withings.com/partner/partner")
				.build();
	}

	@Path("/protecteddata/heart-rate")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response protectedDataHeartRateResponse (
			@QueryParam ("date") String startDate,
			@QueryParam ("end-date") String endDate,
			@HeaderParam(AUTHORIZATION) String bearer) {
		String encryptToken = bearer.substring(bearer.lastIndexOf(" ") + 1 );
		try {
			return Response.status(OK).entity(WithingsPlugin.getHeartRate(encryptToken, startDate, endDate)).build();
		} catch (UnAuthorizedException e) {
			LOG.log(Level.WARNING,"UnAuthorizedException : " + e.getMessage());
			return Response.status(UNAUTHORIZED).build();
		} catch (BadRequestException e) {
			LOG.log(Level.WARNING,"BadRequestException : " + e.getMessage());
			return Response.status(BAD_REQUEST).entity("BadRequestException").build();
		} catch (InternalServerErrorException e) {
			LOG.log(Level.WARNING,"InternalServerErrorException : " + e.getMessage());
			return Response.status(INTERNAL_SERVER_ERROR).entity("InternalServerErrorException").build();
		}
	}
}
