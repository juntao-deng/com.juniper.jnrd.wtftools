package net.juniper.jmp.core.ctx.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.juniper.jmp.core.ctx.IRequest;

public class RequestImpl implements IRequest {
	private HttpServletRequest request;
	public RequestImpl(HttpServletRequest request){
		this.request = request;
	}
	
	@Override
	public String getURI() {
		return request.getRequestURI();
	}

	@Override
	public String getSessionId() {
		return request.getSession().getId();
	}

	@Override
	public String[] getParameters(String key) {
		return request.getParameterValues(key);
	}

	@Override
	public String getParameter(String key) {
		return request.getParameter(key);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return request.getParameterMap();
	}

	@Override
	public String getRemoteAddress() {
		return request.getRemoteAddr();
	}

}
