package com.ecomWebsite.modal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="organization")
public class organization {

	@Id
	private int id;
	private String name;
	private String type;
	private int no_of_emps;
	private float profits;
	private String base;
	
	public organization()
	{
		
	}
	
	public organization(int id,String name,String type,int no_of_emps,float profits,String base)
	{
		super();
		this.id=id;
		this.name=name;
		this.type=type;
		this.no_of_emps=no_of_emps;
		this.profits=profits;
		this.base=base;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}
	
	
}