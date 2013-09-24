package com.juniper.wtf.tags.control;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public abstract class WtfBaseControlTag extends SimpleTagSupport{
	private String id;
	public void doTag() throws JspException, IOException {
		PageContext pageContext = (PageContext) getJspContext();
		String content = readContent();
		pageContext.getOut().write(content);
	}
	
	protected String getWebBasePath() {
		PageContext pageContext = (PageContext) getJspContext();
		String realPath = pageContext.getServletContext().getRealPath("/");
		return realPath + "/library/controls/" + getType();
	}

	protected abstract String readContent();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	protected abstract String getType();
}
