package com.ecomWebsite.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ecomWebsite.modal.organization;;
public interface UserRepository extends CrudRepository<organization,Integer>{
	
/*	@Query(value="IF EXISTS(SELECT MAX(id) FROM u_vendors) "
			+ "BEGIN"
			+ "SELECT MAX(id) FROM u_vendors"
			+"END"
			+ "ELSE"
			+"BEGIN"
			+ "SELECT ISNULL(id,0) FROM u_vendors"
			+ "END",nativeQuery=true)*/
			
//	int findId();

@Modifying
@Query(value="INSERT INTO organization(code,name,country,ph_no,no_of_emps,profits,officeaddress,websiteurl)VALUES(?1,?2,?3,?4,?5,?6,?7,?8)",nativeQuery=true)
public void reg_org(String code, String name, String country, String ph_no,int no_of_emps, float profits, String officeaddress,
		String websiteurl);	

@Modifying
@Query(value="INSERT INTO vendor_org(org_id,org_name,name,username,email,password,ph_no,domain)VALUES(?1,?2,?3,?4,?5,?6,?7,?8)",nativeQuery=true)
public void reg_user(int org_id,String org_name,String name,String username,String email,String password,String ph_no,String domain);

@Query(value="SELECT organization.id FROM organization WHERE organization.name=?1",nativeQuery=true)
public int findId(String name);
}
