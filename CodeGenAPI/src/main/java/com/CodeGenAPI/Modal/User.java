package com.CodeGenAPI.Modal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="vendor_org")
public class User {
	@Id
	private int vendor_id;
	private String name,org_name,email,password;
	private String ph_no,domain,username;
	
	User()
	{
		
	}
	
	public User(String name,String org_name, String email, String password, String ph_no, String domain, String username) {
		super();
		this.name = name;
		this.org_name=org_name;
		this.email = email;
		this.password = password;
		this.ph_no = ph_no;
		this.domain = domain;
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getOrg_name() {
		return org_name;
	}

	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPh_no() {
		return ph_no;
	}

	public void setPh_no(String ph_no) {
		this.ph_no = ph_no;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
