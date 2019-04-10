package main;

import org.glassfish.jersey.uri.UriTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;


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

