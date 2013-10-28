package net.juniper.space.services.demo;

import net.juniper.space.models.demo.device.DemoSecurityStore;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface SecurityStoreService {
	public Page<DemoSecurityStore> getSecurityStores(Specification<DemoSecurityStore> spec, Pageable pagination);
	public DemoSecurityStore getSecurityStore(Integer id);
	public void deleteSecurityStore(Integer id);
	public DemoSecurityStore saveSecurityStore(DemoSecurityStore device);
	public DemoSecurityStore updateSecurityStore(DemoSecurityStore device);
}
