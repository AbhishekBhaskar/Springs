package com.empApp.model;

import java.util.Date;
import java.util.UUID;

public class CodeGenDetails {
	UUID id;
	String hostip;
	int appid,orgid;
	boolean active;
	Date gendate;
	String gentables;
	String keyspacename;
	String projectname;
	String packagename;
	CodeGenDetails()
	{
		
	}
	public CodeGenDetails(UUID id, String hostip, int appid, int orgid, boolean active, Date gendate, String gentables,
			String keyspacename, String projectname, String packagename) {
		super();
		this.id = id;
		this.hostip = hostip;
		this.appid = appid;
		this.orgid = orgid;
		this.active = active;
		this.gendate = gendate;
		this.gentables = gentables;
		this.keyspacename = keyspacename;
		this.projectname = projectname;
		this.packagename = packagename;
	}
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getHostip() {
		return hostip;
	}
	public void setHostip(String hostip) {
		this.hostip = hostip;
	}
	public int getAppid() {
		return appid;
	}
	public void setAppid(int appid) {
		this.appid = appid;
	}
	public int getOrgid() {
		return orgid;
	}
	public void setOrgid(int orgid) {
		this.orgid = orgid;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public Date getGendate() {
		return gendate;
	}
	public void setGendate(Date gendate) {
		this.gendate = gendate;
	}
	public String getGentables() {
		return gentables;
	}
	public void setGentables(String gentables) {
		this.gentables = gentables;
	}
	public String getKeyspacename() {
		return keyspacename;
	}
	public void setKeyspacename(String keyspacename) {
		this.keyspacename = keyspacename;
	}
	public String getProjectname() {
		return projectname;
	}
	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}
	public String getPackagename() {
		return packagename;
	}
	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}
	
	

}
