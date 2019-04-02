package com.CodeGenAPI.Modal;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("remotedb")
public class RemoteDb {
	@Id
	UUID id;
	String hostip;
	boolean active;
	int appid;
	int orgid;
	
	
	RemoteDb()
	{
		
	}

	

	public RemoteDb(UUID id, String hostip, boolean active, int appid, int orgid) {
		super();
		this.id = id;
		this.hostip = hostip;
		this.active = active;
		this.appid = appid;
		this.orgid = orgid;
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



	public boolean isActive() {
		return active;
	}



	public void setActive(boolean active) {
		this.active = active;
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


}
