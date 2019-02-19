package main;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import domaine.oauth.ProtectedDataOauth;
import domaine.oauth2.Oauth2AccessToken;
import domaine.oauth2.Oauth2Authorisation;
import metier.Plugin;
import metier.fitbit.FitbitPlugin;
import pojo.fitbit.Profil;
import pojo.fitbit.hearthrate.HearthRateInterval;

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
	public void revoke(Oauth2AccessToken oauth2AccessToken ) {
		FitbitPlugin.revoke(oauth2AccessToken);
	}
	
	@Path("/protecteddata/profil")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public ProtectedDataOauth<Profil,Oauth2AccessToken> protectedDataProfil (Oauth2AccessToken oauth2AccessToken) {
		return FitbitPlugin.getProfil(oauth2AccessToken);
	}
	
	@Path("/protecteddata/hearthrate")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public ProtectedDataOauth<HearthRateInterval, Oauth2AccessToken> protectedDataHearthRate (@QueryParam ("date") String date,Oauth2AccessToken oauth2AccessToken) {
		LOG.log(Level.INFO,"requesting DataHearthRate");
		return FitbitPlugin.getHearthRate(oauth2AccessToken,date);
	}
	

}
