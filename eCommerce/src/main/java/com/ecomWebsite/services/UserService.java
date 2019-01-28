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
}
