package main;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.OK;

@Path("/test")
public class Test {
	
	private static final Logger LOG = Logger.getLogger(Test.class.getName());
	
	// @Path("/test")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String message() {
		LOG.log(Level.INFO, "Test");
		String template = "http://example.com/{name}/{age}";
		// UriTemplate uriTemplate = new UriTemplate(template);
		Map<String, String> parameters = new HashMap<>();
		parameters.put("name","Arnold");
		parameters.put("age","110");
		UriBuilder builder = UriBuilder.fromPath(template);
		// Use .buildFromMap()
		URI output = builder.buildFromMap(parameters);
		return output.toString();
		
	}




			
}

