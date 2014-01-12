package net.juniper.jmp.mainui.services;

import java.util.List;

import net.juniper.jmp.mainui.home.NavType;
import net.juniper.jmp.monitor.jpa.NavGroupEntity;
import net.juniper.jmp.monitor.jpa.NavItemEntity;
public interface NavTypeService {
    public List<NavGroupEntity> getNavGroups();
    public List<NavItemEntity> getNavItems();
	public List<NavType> getNavList();
}
