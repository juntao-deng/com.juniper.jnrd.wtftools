package net.juniper.jmp.core.ctx;

import net.juniper.jmp.core.util.ParamHelper;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public class ApiContextInfo {
	private IRequest request;
	private PagingContext pagingContext;
	public ApiContextInfo(IRequest request){
		this.request = request;
	}
	
	private Specification<?> getSpecification(){
		return ParamHelper.extractSpecification(request);
	}
	
	private Pageable getPageable() {
		return ParamHelper.extractPageableInfo(request);
	}
	
	public PagingContext getPageContext() {
		if(pagingContext == null){
			pagingContext = new PagingContext();
			pagingContext.setPageable(getPageable());
			pagingContext.setSpec(getSpecification());
		}
		return pagingContext;
	}
}

