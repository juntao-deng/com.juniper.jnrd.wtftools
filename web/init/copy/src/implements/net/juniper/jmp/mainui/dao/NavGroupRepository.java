package net.juniper.jmp.mainui.dao;

import net.juniper.jmp.monitor.jpa.NavGroupEntity;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;

public interface NavGroupRepository extends Repository<NavGroupEntity, Integer> {
	Iterable<NavGroupEntity> findAll(Sort sort);
}
