package com.juniper.wtf.restapp.dashboard;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/dashboard") 
public class DashboardApp {
	
	@GET 
	@Path("/stats") 
	@Produces(MediaType.APPLICATION_JSON)
	public DashboardStats getStats() {
		DashboardStats stats = new DashboardStats();
		stats.setTopMsgTitle("Just a quick note");
		stats.setTopMsgContent("Hope you like this!");
		stats.setJobCount(10);
		stats.setWarningCoung(100);
		stats.setErrorCount(1);
		return stats;
	}
}
