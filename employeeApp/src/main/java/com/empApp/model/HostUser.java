package com.empApp.model;


public class HostUser {
	String hostIp;
	String projectName;
	String packageName;
	String pageId;
	HostUser()
	{
		
	}
	public HostUser(String hostIp,String packageName,String projectName,String pageId) {
		super();
		this.hostIp = hostIp;
		this.packageName = packageName;
		this.projectName = projectName; 
		this.pageId = pageId;
	}
	public String getHostIp() {
		return hostIp;
	}
	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getPageId() {
		return pageId;
	}
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
	

}
