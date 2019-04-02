package com.empApp.Repository;

import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import com.empApp.model.CodeGenDetails;
import org.springframework.data.cassandra.repository.AllowFiltering;


public interface DBDetailsRepository extends CassandraRepository<CodeGenDetails,Integer>{
	//@Query(allowFiltering = true)
	@Query(value="SELECT * FROM remotedb WHERE hostip=?1",allowFiltering=true)
	public void findIp(String hostip);

}
