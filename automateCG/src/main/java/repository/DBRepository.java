package com.automate.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.automate.modal.Database;


public interface DBRepository extends CrudRepository<Database,Integer>{
	

	@Query(value="SELECT * FROM `automate_cg`.`registerdb` WHERE host_ip = ?1",nativeQuery=true)
	public String findByIp(String host_ip);
	
	@Modifying
	@Query(value="INSERT INTO `automate_cg`.`transactions`(host_ip,schema_name,gen_tables,date,time,download_link) VALUES(?1,?2,?3,?4,?5,?6) ",nativeQuery=true)
	public void saveTransaction(String host_ip,String schema_name,String gen_tables,String date,String time,String download_link);

}
