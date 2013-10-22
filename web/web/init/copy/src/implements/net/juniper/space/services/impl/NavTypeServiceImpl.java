package net.juniper.space.services.impl;

import java.util.ArrayList;
import java.util.List;

import net.juniper.space.dao.NavGroupRepository;
import net.juniper.space.dao.NavItemRepository;
import net.juniper.space.models.NavGroup;
import net.juniper.space.models.NavItem;
import net.juniper.space.models.NavType;
import net.juniper.space.services.NavTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
@Service(value="net.juniper.space.services.NavTypeService")
public class NavTypeServiceImpl implements NavTypeService{
    @Autowired
    private NavGroupRepository navGroupRepository;
    @Autowired
    private NavItemRepository navItemRepository;
    
    public List<NavGroup> getNavGroups(){
    	Order order = new Order("id");
    	Sort sort = new Sort(order);
    	return (List<NavGroup>) navGroupRepository.findAll(sort);
    }
    
    public List<NavItem> getNavItems() {
    	Order order = new Order("id");
    	Sort sort = new Sort(order);
    	return (List<NavItem>) navItemRepository.findAll(sort);
    }

	@Override
	public List<NavType> getNavList() {
		List<NavType> allList = new ArrayList<NavType>();
		List<NavGroup> groupList = getNavGroups();
		List<NavItem> itemList = getNavItems();
		allList.addAll(groupList);
		allList.addAll(itemList);
		return allList;
	}
    
}
