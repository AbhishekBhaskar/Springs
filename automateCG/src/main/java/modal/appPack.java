package com.automate.modal;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class appPack {
	@Id
	private String app_name;
	private String package_name;
	
	appPack()
	{
		
	}
	appPack(String app_name,String package_name)
	{
		this.app_name=app_name;
		this.package_name=package_name;
	}
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	public String getPackage_name() {
		return package_name;
	}
	public void setPackage_name(String package_name) {
		this.package_name = package_name;
	}
	
	

}
