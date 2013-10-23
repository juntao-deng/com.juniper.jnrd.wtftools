package net.juniper.space.dao;

import net.juniper.space.models.NavGroup;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;

public interface NavGroupRepository extends Repository<NavGroup, Integer> {
	Iterable<NavGroup> findAll(Sort sort);
}
