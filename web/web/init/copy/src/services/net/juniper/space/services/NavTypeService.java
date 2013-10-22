package net.juniper.space.services;

import java.util.List;

import net.juniper.space.models.NavGroup;
import net.juniper.space.models.NavItem;
import net.juniper.space.models.NavType;
public interface NavTypeService {
    public List<NavGroup> getNavGroups();
    public List<NavItem> getNavItems();
	public List<NavType> getNavList();
}
