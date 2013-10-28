package net.juniper.space.services.demo;

import net.juniper.space.models.demo.device.DemoIpRange;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface IpRangeService {
	public Page<DemoIpRange> getIpRanges(Specification<DemoIpRange> spec, Pageable pagination);
	public DemoIpRange getIpRange(Integer id);
	public void deleteIpRange(Integer id);
	public DemoIpRange saveIpRange(DemoIpRange device);
	public DemoIpRange updateIpRange(DemoIpRange device);
}
