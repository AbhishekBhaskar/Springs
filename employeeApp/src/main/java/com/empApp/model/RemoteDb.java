package com.empApp.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("remotedb")
public class RemoteDb {
	@Id
	UUID id;
	String hostip;
	
	
	RemoteDb()
	{
		
	}

	public RemoteDb(UUID id, String hostip) {
		super();
		this.id = id;
		this.hostip = hostip;
		
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


}
