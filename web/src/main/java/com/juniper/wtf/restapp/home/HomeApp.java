package com.juniper.wtf.restapp.home;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.juniper.wtf.apps.model.NavGroup;
import com.juniper.wtf.apps.model.NavItem;
import com.juniper.wtf.apps.model.NavType;
import com.juniper.wtf.apps.service.NavService;
import com.juniper.wtf.base.SessionBean;

@Path("/home") 
public class HomeApp {
   @Inject
   private NavService navService;
   
   @GET 
   @Path("/homeinfos") 
   @Produces(MediaType.APPLICATION_JSON)
   public HomeInfo getHomeInfo() {
	   HomeInfo homeInfo = new HomeInfo();
	   try{
		   List<NavGroup> groupList = navService.getNavGroups();
		   List<NavItem> itemList = navService.getNavItems();
		   List<NavType> allList = new ArrayList<NavType>();
		   allList.addAll(groupList);
		   allList.addAll(itemList);
		   homeInfo.setNavList(allList);
		   
		   
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
