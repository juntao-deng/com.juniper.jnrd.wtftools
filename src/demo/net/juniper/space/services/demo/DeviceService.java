package net.juniper.space.services.demo;

import net.juniper.space.models.demo.device.DemoDevice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface DeviceService {
	public Page<DemoDevice> getDevices(Specification<DemoDevice> spec, Pageable pagination);
	public DemoDevice getDevice(Integer id);
	public void deleteDevice(Integer id);
	public DemoDevice saveDevice(DemoDevice device);
	public DemoDevice updateDevice(DemoDevice device);
}
