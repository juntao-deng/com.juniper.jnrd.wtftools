package net.juniper.jmp.core.servlet;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

import net.juniper.jmp.core.ctx.impl.RequestImpl;
import net.juniper.jmp.wtf.ctx.ApiContext;
import net.juniper.jmp.wtf.ctx.ApiContextInfo;
import net.juniper.jmp.wtf.ctx.IRequest;

public class WtfRequestContextListener implements ServletRequestListener {

	@Override
	public void requestDestroyed(ServletRequestEvent res) {
		ApiContext.reset();
	}

	@Override
	public void requestInitialized(ServletRequestEvent req) {
		IRequest request = new RequestImpl((HttpServletRequest) req.getServletRequest());
		ApiContextInfo apiContext = new ApiContextInfo(request);
		ApiContext.init(apiContext);
	}

}
