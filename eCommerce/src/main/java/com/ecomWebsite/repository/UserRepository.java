package com.ecomWebsite.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ecomWebsite.modal.organization;

public interface UserRepository extends CrudRepository<organization,Integer>{
	

@Modifying
@Query(value="INSERT INTO organization(code,name,country,ph_no,no_of_emps,profits,officeaddress,websiteurl)VALUES(?1,?2,?3,?4,?5,?6,?7,?8)",nativeQuery=true)
public void reg_org(String code, String name, String country, String ph_no,int no_of_emps, float profits, String officeaddress,
		String websiteurl);	

@Modifying
@Query(value="INSERT INTO vendor_org(org_id,org_name,name,username,email,password,ph_no,domain)VALUES(?1,?2,?3,?4,?5,?6,?7,?8)",nativeQuery=true)
public void reg_user(int org_id,String org_name,String name,String username,String email,String password,String ph_no,String domain);

@Modifying
@Query(value="INSERT INTO products(org_id,pr_code,pr_name,pr_url,license_number,cost_per_item,created,warranty_period) VALUES(?1,?2,?3,?4,?5,?6,?7,?8)",nativeQuery=true)
public void reg_product(int org_id,String pr_code,String pr_name,String pr_url,String license_number,float cost_per_item,String created,int warranty_period);

@Query(value="SELECT organization.id FROM organization WHERE organization.name=?1",nativeQuery=true)
public int findId(String name);


@Query(value="SELECT pr_code FROM products WHERE pr_code=?1",nativeQuery=true)
public String checkCode(String code);



@Query(value="SELECT org_id FROM vendor_org WHERE username LIKE ?1 AND password LIKE ?2",nativeQuery=true)
public int findloggedinId(String username,String password);

}
