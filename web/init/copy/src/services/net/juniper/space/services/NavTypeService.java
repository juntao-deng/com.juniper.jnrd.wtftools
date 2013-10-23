package net.juniper.space.services;

import java.util.List;

import net.juniper.space.models.home.NavGroup;
import net.juniper.space.models.home.NavItem;
import net.juniper.space.models.home.NavType;
public interface NavTypeService {
    public List<NavGroup> getNavGroups();
    public List<NavItem> getNavItems();
	public List<NavType> getNavList();
}
