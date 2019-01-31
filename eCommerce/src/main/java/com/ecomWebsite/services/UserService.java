package com.ecomWebsite.services;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ecomWebsite.modal.organization;
import com.ecomWebsite.repository.UserRepository;

@Service
@Transactional
public class UserService {

	private final UserRepository userRepository;
	
	public UserService(UserRepository userRepository)
	{
		this.userRepository=userRepository;
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
	
	public int findtheId(String org_name)
	{
		int id=userRepository.findId(org_name);
		return id;
	}
	
}
