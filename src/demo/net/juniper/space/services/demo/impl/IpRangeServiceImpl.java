package net.juniper.space.services.demo.impl;
import net.juniper.space.demo.dao.IpRangeRepository;
import net.juniper.space.models.demo.device.DemoIpRange;
import net.juniper.space.services.demo.IpRangeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
@Service(value="net.juniper.space.services.demo.IpRangeService")
public class IpRangeServiceImpl implements IpRangeService{
	@Autowired
	private IpRangeRepository dao;
	
	@Override
	public DemoIpRange getIpRange(Integer id) {
		return dao.findOne(id);
	}

	@Override
	public void deleteIpRange(Integer id) {
		dao.delete(id);
	}

	@Override
	public DemoIpRange saveIpRange(DemoIpRange device) {
		return dao.save(device);
	}

	@Override
	public DemoIpRange updateIpRange(DemoIpRange device) {
		return dao.save(device);
	}

	@Override
	public Page<DemoIpRange> getIpRanges(Specification<DemoIpRange> spec, Pageable pagination) {
		return dao.findAll(spec, pagination);
	}
	
}
