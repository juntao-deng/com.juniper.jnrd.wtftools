package com.juniper.wtf.tags;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

/**
 * Foward a servlet url to mapped jsp. The default rule is : 
 * servlet path '/ctxpath/apps/home' ==> '/applications/home/home.jsp'
 * @author juntaod
 *
 */
public class AppDispatchServlet extends HttpServlet {
	private static final String PAGE_PATH = "PAGE_PATH";
	private static final long serialVersionUID = -5849457629464390672L;
	private static final String BASE_PATH = "/applications/";
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		if(pathInfo == null || pathInfo.indexOf("/", 1) != -1){
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		pathInfo = pathInfo.substring(1);
		String path = BASE_PATH + pathInfo + "/" + pathInfo + ".jsp";
		//The jsp page will use this as a navgation
		req.setAttribute(PAGE_PATH, StringUtils.capitalize(pathInfo));
		req.getServletContext().getRequestDispatcher(path).forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.getWriter().write("the post method is not allowed to be requested");
	}

}
