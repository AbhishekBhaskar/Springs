package com.automate.repository;
import com.automate.modal.User;
import com.automate.services.*;

import java.util.List;

import javax.persistence.Table;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Integer>{

	@Query(value="SELECT * FROM information_schema.tables WHERE TABLE_SCHEMA IN ('mysql','information_schema','performance_schema') ORDER BY engine DESC",nativeQuery=true)
	public List<Table> get_tables();
}
