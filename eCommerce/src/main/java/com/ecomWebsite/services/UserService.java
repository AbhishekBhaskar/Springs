package com.ecomWebsite.services;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ecomWebsite.modal.organization;
import com.ecomWebsite.repository.ProductRepository;
import com.ecomWebsite.repository.UserRepository;
import com.ecomWebsite.modal.User;

@Service
@Transactional
public class UserService {

	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	
	public UserService(UserRepository userRepository,ProductRepository productRepository)
	{
		this.userRepository=userRepository;
		this.productRepository=productRepository;
	}
	
	public void saveOrg(organization org)
	{
		userRepository.save(org);
	}
	
/*	public int findByID()
	{
		return userRepository.findId();
	}  */
	
	public void savOrganization(String code, String name, String country, String ph_no,int no_of_emps, float profits, String officeaddress,
		String websiteurl)
	{
		userRepository.reg_org(code, name, country, ph_no, no_of_emps, profits, officeaddress, websiteurl);
	}
	
	public void saveUser(int org_id,String org_name,String name,String username,String email,String password,String ph_no,String domain)
	{
		userRepository.reg_user(org_id, org_name, name, username, email, password, ph_no, domain);
	}
	
	public void saveProd(int org_id,String pr_code,String pr_name,String pr_url,String license_number,float cost_per_item,String created,int warranty_period)
	{
		userRepository.reg_product(org_id, pr_code, pr_name, pr_url, license_number, cost_per_item, created, warranty_period);
	}
	
	public int findtheId(String org_name)
	{
		int id=userRepository.findId(org_name);
		return id;
	}
	
	public int findloggedId(String username,String password)
	{
		int id=userRepository.findloggedinId(username, password);
		return id;
	}
	
	public User findByUsernameAndPassword(String username,String password)
	{
		return productRepository.findByUsernameAndPassword(username, password);
	}
	
	public int checkCode(String code)
	{
		String c=userRepository.checkCode(code);
		if(c!=null)
			return 1;
		else
			return 0;
	}
}
