package com.CodeGenAPI.Repository;

import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import com.CodeGenAPI.Modal.CodeGenDetails;
import org.springframework.data.cassandra.repository.AllowFiltering;


public interface PicocogDBRepo extends CassandraRepository<CodeGenDetails,Integer>{
	//@Query(allowFiltering = true)
	@Query(value="SELECT * FROM remotedb WHERE hostip=?1",allowFiltering=true)
	public void findIp(String hostip);

}
