package main;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;




@Path("/test")
public class Test {
		
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String test() {
		return "it works";
	}
			
}

