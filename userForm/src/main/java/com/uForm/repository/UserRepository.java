package com.uForm.repository;

import org.springframework.data.repository.CrudRepository;

import com.uForm.modal.User;

public interface UserRepository extends CrudRepository<User, Integer> {
	public User findByUsernameAndPassword(String username,String password);
	
}
