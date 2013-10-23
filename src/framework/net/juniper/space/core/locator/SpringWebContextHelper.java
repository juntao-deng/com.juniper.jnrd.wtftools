package net.juniper.space.core.locator;

import org.springframework.web.context.WebApplicationContext;

public final class SpringWebContextHelper {
	private static WebApplicationContext springCtx;
	public static void setSpringCtx(WebApplicationContext ctx){
		springCtx = ctx;
	}
	
	public static WebApplicationContext getSpringCtx() {
		return springCtx;
	}
}
