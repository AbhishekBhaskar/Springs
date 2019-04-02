package com.CodeGenAPI.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.CodeGenAPI.Modal.Database;


public interface DBRepository extends CrudRepository<Database,Integer>{
	

	@Query(value="SELECT * FROM `autocg`.`remotedb` WHERE hostip = ?1",nativeQuery=true)
	public String findByIp(String host_ip);
	
	@Modifying
	@Query(value="INSERT INTO `remotedb`(remotedbname,hostip,username,password,appid,orgid) VALUES(?1,?2,?3,?4,?5,?6)",nativeQuery=true)
	public void saveDb(String remotedbname,String hostip,String username,String password,int appid,int orgid);
	
	@Modifying
	@Query(value="INSERT INTO `autocg`.`codegendetails`(hostip,schemaname,gentables,gendate,downloadlink,appname,packagename,appid,orgid) VALUES(?1,?2,?3,?4,?5,?6,?7,?8,?9) ",nativeQuery=true)
	public void saveTransaction(String host_ip,String schema_name,String gen_tables,String datetime,String download_link,String app_name,String package_name,int appid,int orgid);

}
