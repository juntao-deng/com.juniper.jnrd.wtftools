package net.juniper.space.demo.dao;

import net.juniper.space.models.demo.device.DemoSecurityStore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SecurityStoreRepository extends JpaRepository<DemoSecurityStore, Integer>, JpaSpecificationExecutor<DemoSecurityStore>{

}
