package com.juniper.wtf.tags;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class HtmlBaseTag extends SimpleTagSupport{
		
	public void doTag() throws JspException, IOException {
		PageContext pageContext = (PageContext) getJspContext();
		ServletRequest req = pageContext.getRequest();
		String addr = HtmlBaseHelper.getBaseStr((HttpServletRequest) req);
		pageContext.getOut().write(addr);
	}
}
