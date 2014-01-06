package net.juniper.jmp.core.ctx;

import net.juniper.jmp.core.util.ParamHelper;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public class ApiContextInfo {
	private IRequest request;
	private PagingContext pagingContext;
	private ClientInfo clientInfo;
	public ApiContextInfo(IRequest request){
		this.request = request;
	}
	
	private Specification<?> getSpecification(){
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
			pagingContext.setSpec(getSpecification());
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

