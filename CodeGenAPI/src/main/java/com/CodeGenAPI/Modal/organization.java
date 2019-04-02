package com.CodeGenAPI.Modal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="organization")
public class organization {

	@Id
	private int id;
	private String code;
	private String name;
	private String country;
	private String ph_no;
	private int no_of_emps;
	private float profits;
	private String officeaddress;
	private String websiteurl;
	
	public organization()
	{
		
	}

	public organization(String code, String name, String country, String ph_no,int no_of_emps, float profits, String officeaddress,
			String websiteurl) {
		super();
		this.code = code;
		this.name = name;
		this.country=country;
		this.ph_no=ph_no;
		this.no_of_emps = no_of_emps;
		this.profits = profits;
		this.officeaddress = officeaddress;
		this.websiteurl = websiteurl;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPh_no() {
		return ph_no;
	}

	public void setPh_no(String ph_no) {
		this.ph_no = ph_no;
	}

	public int getNo_of_emps() {
		return no_of_emps;
	}

	public void setNo_of_emps(int no_of_emps) {
		this.no_of_emps = no_of_emps;
	}

	public float getProfits() {
		return profits;
	}

	public void setProfits(float profits) {
		this.profits = profits;
	}

	public String getOfficeaddress() {
		return officeaddress;
	}

	public void setOfficeaddress(String officeaddress) {
		this.officeaddress = officeaddress;
	}

	public String getWebsiteurl() {
		return websiteurl;
	}

	public void setWebsiteurl(String websiteurl) {
		this.websiteurl = websiteurl;
	}
	
	

	
	
}
