package com.juniper.wtf.restapp.dashboard;

public class DashboardStats{
	private Integer jobCount;
	private Integer errorCount;
	private Integer warningCoung;
	private String topMsgTitle;
	private String topMsgContent;
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
