package net.juniper.jmp.core.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.juniper.jmp.core.locator.SpringWebContextHelper;

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
