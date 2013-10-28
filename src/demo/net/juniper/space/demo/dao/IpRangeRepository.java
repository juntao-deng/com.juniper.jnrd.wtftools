package net.juniper.space.demo.dao;

import net.juniper.space.models.demo.device.DemoIpRange;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IpRangeRepository extends JpaRepository<DemoIpRange, Integer>, JpaSpecificationExecutor<DemoIpRange>{

}
