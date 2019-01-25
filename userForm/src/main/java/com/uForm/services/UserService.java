package com.uForm.services;



import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.uForm.modal.Todo;
import com.uForm.modal.User;
import com.uForm.repository.UserRepository;
import com.uForm.repository.todoRepository;

@Service
@Transactional
public class UserService {

	private final UserRepository userRepository;
	private final todoRepository todorepo;
	
	public UserService(UserRepository userRepository,todoRepository todoRepo)
	{
		this.userRepository=userRepository;
		this.todorepo=todoRepo;
	}
	
	public void saveMyUser(User user)
	{
		userRepository.save(user);
	}
	
	public void savetodo(Todo td)
	{
		todorepo.save(td);
	}
	
	public List<User> showAllUsers()
	{
		List<User> users = new ArrayList<User>();
		for(User user:userRepository.findAll())
		{
			users.add(user);
		}
		return users;
	}
	
	public void deleteMyUser(int id)
	{
		userRepository.deleteById(id);
	}
	
	public User editUser(int id)
	{
		/*User u=userRepository.findById(id).get();
		userRepository.save(u);*/
		return userRepository.findById(id).orElse(null);
	}
	
	public User findByUsernameAndPassword(String username,String password)
	{
		return userRepository.findByUsernameAndPassword(username, password);
	}
	
/*	public User findID()
	{
		User u;
		List<User> users=new ArrayList<User>();
		for(User user:userRepository.findAll())
		{
			if()
		}
	}  */
	
	
	
	public static List<Todo> todos=new ArrayList<Todo>();
	
	public void settodos(int id,String user,String desc,Date targetDate,boolean isDone)
	{
		
		todos.add(new Todo(id,user,desc,targetDate,isDone));
		
	}
	
	
	public List<Todo> gettodos()
	{
		List<Todo> userTodo=new ArrayList<Todo>();
		for(Todo td:todos)
		{
			userTodo.add(td);
		}
		
		return userTodo;
	}
	
}
