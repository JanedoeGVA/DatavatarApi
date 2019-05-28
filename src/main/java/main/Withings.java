package main;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

import domaine.ActivityTracker;
import domaine.oauth2.Oauth2AccessToken;
import metier.exception.UnAuthorizedException;

import metier.withings.WithingsPlugin;
import org.json.JSONException;
import org.json.JSONObject;
import outils.Constant;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.Status.*;

@Path("/withings")
public class Withings {

	private static final Logger LOG = Logger.getLogger(Withings.class.getName());

	@Path("/authorization")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response authorisation() {
		return Response.status(OK).entity(WithingsPlugin.urlVerification()).build();
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
	public Response refresh (@HeaderParam("Authorization") String bearer) {
		String encryptToken = bearer.substring(bearer.lastIndexOf(" ") + 1 );
		LOG.log(Level.INFO,"crypted token" + encryptToken);
		Oauth2AccessToken oauth2AccessToken = WithingsPlugin.refresh(encryptToken);
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

    @Path("/revoke-method")
    @GET
    public Response getRevokeMethod() {
		LOG.log(Level.INFO,"revoke-method called");
		final String json = "{\"method\":\"web\",\"uri\":\"" + Constant.WITHINGS_URL_UI_REVOKE + "\"}";
        return Response.status(OK).entity(json).build();
    }

	// Could return SEE_OTHER (302)
	// TODO set revoke method and url in api.properties file ?????
	@Path("/revoke")
	@POST
	public Response revoke(@HeaderParam(AUTHORIZATION) String bearer) {
		String encryptToken = bearer.substring(bearer.lastIndexOf(" ") + 1 );
		LOG.log(Level.INFO,"crypted token" +encryptToken);
		try {
			// Could return code SEE_OTHER (302) if revoke not implemented
			WithingsPlugin.revoke(encryptToken);
			return Response.status(OK).build();
		} catch (IOException | InterruptedException | ExecutionException e) {
			LOG.log(Level.SEVERE,"Error during revoking token : " , e);
			return Response.serverError().build();
		}
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
			LOG.log(Level.WARNING,"InternalServerErrorException : " ,e);
			return Response.status(INTERNAL_SERVER_ERROR).entity("InternalServerErrorException").build();
		}
	}
}
