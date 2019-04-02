package com.CodeGenAPI.Modal;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("codegendetails")
public class CodeGenDetails {
	@Id
	int userid;
	UUID uuid;
	String hostip;
	int appid,orgid;
	boolean active;
	String gendate;
	String gentables;
	String keyspacename;
	String projectname;
	String packagename;
	CodeGenDetails()
	{
		
	}
	public CodeGenDetails(int userid,UUID uuid, String hostip, int appid, int orgid, boolean active, String gendate, String gentables,
			String keyspacename, String projectname, String packagename) {
		super();
		this.userid = userid;
		this.uuid = uuid;
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
	
	
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	
	public UUID getUuid() {
		return uuid;
	}
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
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
	public String getGendate() {
		return gendate;
	}
	public void setGendate(String gendate) {
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
