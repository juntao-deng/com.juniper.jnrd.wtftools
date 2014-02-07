package net.juniper.jmp.wtf.ctx;

import net.juniper.jmp.core.util.ParamHelper;

public class ApiContextInfo {
	private IRequest request;
	private PagingContext pagingContext;
	private ClientInfo clientInfo;
	public ApiContextInfo(IRequest request){
		this.request = request;
	}
	
	private Filterable getFilterable(){
		return ParamHelper.extractSpecification(request);
	}
	
	private Pageable getPageable() {
		return ParamHelper.extractPageableInfo(request);
	}
	
	public IRequest getRequest() {
		return request;
	}
	
	public PagingContext getPageContext() {
		if(pagingContext == null){
			pagingContext = new PagingContext();
			pagingContext.setPageable(getPageable());
			pagingContext.setFilter(getFilterable());
		}
		return pagingContext;
	}

	public ClientInfo getClientInfo() {
		if(clientInfo == null){
			clientInfo = new ClientInfo();
			String clientIp = request.getRemoteAddress();
			clientInfo.setClientIp(clientIp);
		}
		return clientInfo;
	}
}

