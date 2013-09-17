package com.juniper.wtf.restapp.home;

import java.util.List;

import com.juniper.wtf.apps.model.NavType;
import com.juniper.wtf.base.SessionBean;

public class HomeInfo {
	private List<NavType> navList;
	private SessionBean sessionBean;
	private Integer jobCount;
	private Integer errorCount;
	private Integer warningCoung;
	private String topMsgTitle;
	private String topMsgContent;
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
	public Integer getJobCount() {
		return jobCount;
	}
	public void setJobCount(Integer jobCount) {
		this.jobCount = jobCount;
	}
	public Integer getErrorCount() {
		return errorCount;
	}
	public void setErrorCount(Integer errorCount) {
		this.errorCount = errorCount;
	}
	public Integer getWarningCoung() {
		return warningCoung;
	}
	public void setWarningCoung(Integer warningCoung) {
		this.warningCoung = warningCoung;
	}
	public String getTopMsgTitle() {
		return topMsgTitle;
	}
	public void setTopMsgTitle(String topMsgTitle) {
		this.topMsgTitle = topMsgTitle;
	}
	public String getTopMsgContent() {
		return topMsgContent;
	}
	public void setTopMsgContent(String topMsgContent) {
		this.topMsgContent = topMsgContent;
	}
}
