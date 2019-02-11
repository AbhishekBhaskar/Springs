package com.automate.services;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.automate.repository.UserRepository;
import java.util.List;

import javax.persistence.Table;

@Service
@Transactional
public class UserService {
	private final UserRepository userRepository;
	
	public UserService(UserRepository userRepository)
	{
		this.userRepository=userRepository;
	}
	
	public List<Table> get_tables()
	{
		return userRepository.get_tables();
	}

}
