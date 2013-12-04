package net.juniper.space.core.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.juniper.space.core.locator.SpringWebContextHelper;

import org.springframework.web.context.support.WebApplicationContextUtils;
/**
 * 
 * @author juntaod
 *
 */
public class WtfContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		SpringWebContextHelper.setSpringCtx(WebApplicationContextUtils.getWebApplicationContext(event.getServletContext()));
	}
	
}
