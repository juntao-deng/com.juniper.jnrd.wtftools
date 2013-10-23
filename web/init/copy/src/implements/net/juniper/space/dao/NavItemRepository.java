package net.juniper.space.dao;

import net.juniper.space.models.NavItem;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;

public interface NavItemRepository extends Repository<NavItem, Integer> {
	Iterable<NavItem> findAll(Sort sort);
}
