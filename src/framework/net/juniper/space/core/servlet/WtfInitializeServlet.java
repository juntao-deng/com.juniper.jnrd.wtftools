package net.juniper.space.core.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.juniper.space.core.locator.SpringWebContextHelper;

import org.springframework.web.context.support.WebApplicationContextUtils;

public class WtfInitializeServlet extends HttpServlet {
	private static final long serialVersionUID = 6658945385630343099L;
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringWebContextHelper.setSpringCtx(WebApplicationContextUtils.getWebApplicationContext(config.getServletContext()));
		
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.service(req, resp);
	}
	
}
