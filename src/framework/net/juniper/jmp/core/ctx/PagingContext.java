package net.juniper.jmp.core.ctx;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public class PagingContext {
	private Specification<?> spec;
	private Pageable pageable;
	
	public Specification<?> getSpec() {
		return spec;
	}
	public <T>Specification<T> getSpec(Class<T> entityClass) {
		return (Specification<T>) spec;
	}
	
	public void setSpec(Specification<?> spec) {
		this.spec = spec;
	}
	public Pageable getPageable() {
		return pageable;
	}
	public void setPageable(Pageable pageable) {
		this.pageable = pageable;
	}
}
