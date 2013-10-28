package net.juniper.space.services.demo.impl;
import net.juniper.space.demo.dao.DeviceRepository;
import net.juniper.space.models.demo.device.DemoDevice;
import net.juniper.space.services.demo.DeviceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
@Service(value="net.juniper.space.services.demo.DeviceService")
public class DeviceServiceImpl implements DeviceService{
	@Autowired
	private DeviceRepository dao;
	
	@Override
	public DemoDevice getDevice(Integer id) {
		return dao.findOne(id);
	}

	@Override
	public void deleteDevice(Integer id) {
		dao.delete(id);
	}

	@Override
	public DemoDevice saveDevice(DemoDevice device) {
		return dao.save(device);
	}

	@Override
	public DemoDevice updateDevice(DemoDevice device) {
		return dao.save(device);
	}

	@Override
	public Page<DemoDevice> getDevices(Specification<DemoDevice> spec, Pageable pagination) {
		return dao.findAll(spec, pagination);
	}
	
}
