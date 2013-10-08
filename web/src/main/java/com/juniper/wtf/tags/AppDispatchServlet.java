package com.juniper.wtf.tags;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Foward a servlet url to mapped jsp. The default rule is : 
 * servlet path '/ctxpath/apps/home' ==> '/applications/home/home.jsp'
 * @author juntaod
 *
 */
public class AppDispatchServlet extends HttpServlet {
//	private static final String PAGE_PATH = "PAGE_PATH";
	private static final long serialVersionUID = -5849457629464390672L;
	private static final String BASE_PATH = "/applications/";
	private static Map<String, Pair> jsMap = new ConcurrentHashMap<String, Pair>();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		if(pathInfo == null || pathInfo.equals("")){
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		pathInfo = pathInfo.substring(1);
		String basePath = null;
		if(pathInfo.indexOf("/") != -1){
			String[] paths = pathInfo.split("/");
			basePath = BASE_PATH + StringUtils.join(paths, "/") + "/" + paths[paths.length - 1];
		}
		else
			basePath = BASE_PATH + pathInfo + "/" + pathInfo;
		//The jsp page will use this as a navgation
//		req.setAttribute(PAGE_PATH, StringUtils.capitalize(pathInfo));
		req.getServletContext().getRequestDispatcher(basePath + ".html").forward(req, resp);
		String content = getControllerContent(req, basePath);
		if(content != null){
			resp.getOutputStream().write(getSignLine());
			resp.getOutputStream().write(content.getBytes());
		}
	}

	private byte[] getSignLine() {
		return "//js sign start ======".getBytes();
	}

	private String getControllerContent(HttpServletRequest req, String basePath) throws IOException {
		String realPath = req.getServletContext().getRealPath("/");
		String jsPath = realPath + basePath + ".js";
		File f = new File(jsPath);
		if(f.exists()){
			long lastModify = f.lastModified();
			Pair content = jsMap.get(jsPath);
			if(content != null){
				if(content.lastmodified == lastModify)
					return content.content;
			}
			String fContent = FileUtils.readFileToString(f);
			content = new Pair();
			content.content = fContent;
			content.lastmodified = lastModify;
			jsMap.put(jsPath, content);
			return fContent;
		}
		return null;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.getWriter().write("the post method is not allowed to be requested");
	}
	
	class Pair{
		String content;
		long lastmodified;
	}

}
