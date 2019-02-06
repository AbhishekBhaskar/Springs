package com.ecomWebsite.repository;

import org.springframework.data.repository.CrudRepository;

import com.ecomWebsite.modal.User;

public interface ProductRepository extends CrudRepository<User,Integer>{

	public User findByUsernameAndPassword(String username,String password);
}
