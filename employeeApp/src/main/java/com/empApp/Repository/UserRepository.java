package com.empApp.Repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
//import org.springframework.data.jpa.repository.Modifying;

import com.empApp.model.RemoteDb;
import com.empApp.model.User;

public interface UserRepository extends CassandraRepository<RemoteDb, UUID>{

	
	@Query(value="INSERT INTO `remotedb`(id,active,appid,hostip,orgid) VALUES(?1,?2,?3,?4,?5)",allowFiltering=true)
	public void saveUser(UUID id,boolean active,int appid,String hostip,int orgid);
	

}
