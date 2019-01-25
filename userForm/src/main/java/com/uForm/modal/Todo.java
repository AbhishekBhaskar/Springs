package com.uForm.modal;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="todo")
public class Todo
{
	@Id
	private int id;
	private String user,desc;
	private Date targetDate;
	private boolean isDone;
	
	public Todo()
	{
		
	}
	
	public Todo(int id,String user,String desc,Date targetDate,boolean isDone)
	{
		super();
		this.id=id;
		this.user=user;
		this.desc=desc;
		this.targetDate=targetDate;
		this.isDone=isDone;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}
	
	
}