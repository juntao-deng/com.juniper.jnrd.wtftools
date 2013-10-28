package net.juniper.space.services.demo.impl;
import net.juniper.space.demo.dao.SecurityStoreRepository;
import net.juniper.space.models.demo.device.DemoSecurityStore;
import net.juniper.space.services.demo.SecurityStoreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
@Service(value="net.juniper.space.services.demo.SecurityStoreService")
public class SecurityStoreServiceImpl implements SecurityStoreService{
	@Autowired
	private SecurityStoreRepository dao;
	
	@Override
	public DemoSecurityStore getSecurityStore(Integer id) {
		return dao.findOne(id);
	}

	@Override
	public void deleteSecurityStore(Integer id) {
		dao.delete(id);
	}

	@Override
	public DemoSecurityStore saveSecurityStore(DemoSecurityStore device) {
		return dao.save(device);
	}

	@Override
	public DemoSecurityStore updateSecurityStore(DemoSecurityStore device) {
		return dao.save(device);
	}

	@Override
	public Page<DemoSecurityStore> getSecurityStores(Specification<DemoSecurityStore> spec, Pageable pagination) {
		return dao.findAll(spec, pagination);
	}
	
}
