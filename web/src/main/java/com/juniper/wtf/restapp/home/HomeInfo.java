package com.juniper.wtf.restapp.home;

import java.util.List;

import com.juniper.wtf.apps.model.NavType;
import com.juniper.wtf.base.SessionBean;

public class HomeInfo {
	private List<NavType> navList;
	private SessionBean sessionBean;
	public List<NavType> getNavList() {
		return navList;
	}
	public void setNavList(List<NavType> navList) {
		this.navList = navList;
	}
	public SessionBean getSessionBean() {
		return sessionBean;
	}
	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}
}
