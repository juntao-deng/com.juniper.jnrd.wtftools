package net.juniper.jmp.mainui.restful;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.juniper.jmp.mainui.home.HomeInfo;

@Path("/homeinfos") 
public interface HomeRestService {
   @GET 
   @Path("/") 
   @Produces(MediaType.APPLICATION_JSON)
   public HomeInfo getHomeInfo();
} 
