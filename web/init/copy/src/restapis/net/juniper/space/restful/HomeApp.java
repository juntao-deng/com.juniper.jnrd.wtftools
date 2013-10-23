package net.juniper.space.restful;
import java.util.Calendar;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.juniper.space.core.locator.ServiceLocator;
import net.juniper.space.models.NavType;
import net.juniper.space.services.NavTypeService;
import net.juniper.space.sys.SessionBean;

@Path("/homeinfos") 
public class HomeApp {
   @GET 
   @Path("/") 
   @Produces(MediaType.APPLICATION_JSON)
   public HomeInfo getHomeInfo() {
	   HomeInfo homeInfo = new HomeInfo();
	   try{
		   List<NavType> groupList = ServiceLocator.getService(NavTypeService.class).getNavList();
		   homeInfo.setNavList(groupList);
		   
		   
		   SessionBean sb = new SessionBean();
		   sb.setLoginDate(Calendar.getInstance().getTime());
		   sb.setUserCode("super");
		   sb.setUserId("super");
		   homeInfo.setSessionBean(sb);
		   return homeInfo;
	   }
	   catch(Throwable e){
		   e.printStackTrace();
		   return homeInfo;
	   }
	   
   } 
} 
