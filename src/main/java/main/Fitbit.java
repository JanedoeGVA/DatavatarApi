package main;

import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.cars.framework.secrets.DockerSecretLoadException;
import com.cars.framework.secrets.DockerSecrets;

import domaine.oauth.ProtectedDataOauth;
import domaine.oauth2.Oauth2AccessToken;
import domaine.oauth2.Oauth2Authorisation;
import domaine.oauth2.ProtectedDataOauth2;
import metier.fitbit.FitbitPlugin;
import pojo.fitbit.Profil;

@Path("/fitbit")
public class Fitbit {
	
	@Path("/test")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String test() {
		try {
			Map<String, String> secrets = DockerSecrets.loadFromFile("fitbit");
			
			//String clientId = secrets.get("client_id");
			//System.out.println(clientId); // readonly
			//return "it works !!! Client id=" + clientId;
			return "it works !!";
		} catch (DockerSecretLoadException e) {
			e.printStackTrace();
			return "catch " + e;	
		}
	}
	
	@Path("/authorisation")
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
		System.out.println("Coucou1");
		FitbitPlugin.revoke(oauth2AccessToken);
	}
	
	@Path("/protecteddata/profil")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public ProtectedDataOauth<Profil,Oauth2AccessToken> protectedDataProfil (Oauth2AccessToken oauth2AccessToken) {
		return FitbitPlugin.getProfil(oauth2AccessToken);
	}
	
	

}
