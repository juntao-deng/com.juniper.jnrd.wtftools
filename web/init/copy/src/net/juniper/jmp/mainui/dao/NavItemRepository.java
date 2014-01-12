package net.juniper.jmp.mainui.dao;

import net.juniper.jmp.monitor.jpa.NavItemEntity;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;

public interface NavItemRepository extends Repository<NavItemEntity, Integer> {
	Iterable<NavItemEntity> findAll(Sort sort);
}
