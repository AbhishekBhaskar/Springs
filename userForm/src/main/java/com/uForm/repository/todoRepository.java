package com.uForm.repository;

import org.springframework.data.repository.CrudRepository;

import com.uForm.modal.Todo;

public interface todoRepository extends CrudRepository<Todo,Integer>{
	
}
