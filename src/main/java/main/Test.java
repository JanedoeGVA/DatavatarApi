package main;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import metier.fitbit.FitbitApi_OAuth20_ServiceImpl;




@Path("/test")
public class Test {
	
	private static final Logger LOG = Logger.getLogger(Test.class.getName());
	

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String test() {
		LOG.log(Level.INFO, "Test");
		return "it works";
		
	}
			
}

