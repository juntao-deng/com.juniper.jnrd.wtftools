package com.juniper.wtf.apps.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.juniper.wtf.apps.model.NavGroup;
import com.juniper.wtf.apps.model.NavItem;

@Stateless
public class NavService {
//	@Inject
//    private Logger log;

    @PersistenceContext
    private EntityManager em;
    
    public List<NavGroup> getNavGroups(){
    	Query q = em.createNativeQuery("select * from wtf_navgroup order by id asc", NavGroup.class);
    	return q.getResultList();
    }
    
    public List<NavItem> getNavItems() {
    	Query q = em.createNativeQuery("select * from wtf_navitem order by id asc", NavItem.class);
    	return q.getResultList();
    }
    
}
