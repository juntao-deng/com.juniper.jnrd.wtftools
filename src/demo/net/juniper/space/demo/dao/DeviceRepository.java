package net.juniper.space.demo.dao;

import net.juniper.space.models.demo.device.DemoDevice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DeviceRepository extends JpaRepository<DemoDevice, Integer>, JpaSpecificationExecutor<DemoDevice>{

}
