package net.juniper.jmp.core.ctx;

import org.springframework.data.jpa.domain.Specification;

public class ApiContextInfo {
	private IRequest request;
	public ApiContextInfo(IRequest request){
		this.request = request;
	}
	
	public <T>Specification<T> getSpecification(Class<T> entityClazz){
		return readFromRequest(entityClazz);
	}
	
	private <T>Specification<T> readFromRequest(Class<T> entityClazz) {
		String uri = request.getURI();
//		PathSegment seg = new PageSegment
		return null;
	}
}
