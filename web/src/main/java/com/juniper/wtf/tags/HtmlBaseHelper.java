package com.juniper.wtf.tags;

import javax.servlet.http.HttpServletRequest;

public class HtmlBaseHelper {
	public static String getBaseStr(HttpServletRequest req){
		String rootPath = (String)req.getSession().getServletContext().getContextPath();
		String baseAddr = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort();
		String addr = "<base href=\"" + baseAddr + rootPath;
		addr += "/\" />";
		return addr;
	}
}
