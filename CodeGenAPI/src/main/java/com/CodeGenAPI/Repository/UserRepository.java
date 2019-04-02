package com.CodeGenAPI.Repository;
import com.CodeGenAPI.Modal.User;
import com.CodeGenAPI.Services.*;

import java.util.List;
import java.util.Objects;

import javax.persistence.Table;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Integer>{

	@Query(value="SELECT * FROM information_schema.tables WHERE TABLE_TYPE='BASE TABLE' AND TABLE_SCHEMA='e-comm'",nativeQuery=true)
	public List<String> get_tables();
	
}
