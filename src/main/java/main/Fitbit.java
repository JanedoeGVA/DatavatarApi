package main;

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
import metier.fitbit.FitbitPlugin;
import pojo.fitbit.Profil;

@Path("/fitbit")
public class Fitbit {
	
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
	
	

}
