package com.automate.modal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="remotedb")
public class Database {

	@Id
	private int id;
	private String registered_name,host_ip,username,password;
	int appid,orgid;
	
	public Database()
	{
		
	}

	public Database(String registered_name, String host_ip, String username, String password,int appid,int orgid) {
		super();
		this.registered_name = registered_name;
		this.host_ip = host_ip;
		this.username = username;
		this.password = password;
		this.appid=appid;
		this.orgid=orgid;
	}

	public String getRegistered_name() {
		return registered_name;
	}

	public void setRegistered_name(String registered_name) {
		this.registered_name = registered_name;
	}

	public String getHost_ip() {
		return host_ip;
	}

	public void setHost_ip(String host_ip) {
		this.host_ip = host_ip;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
