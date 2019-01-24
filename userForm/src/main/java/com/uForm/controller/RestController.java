package com.uForm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.uForm.modal.User;
import com.uForm.services.UserService;

@org.springframework.web.bind.annotation.RestController
public class RestController {

	@Autowired
	private UserService userService;
	@GetMapping("/")
	public String Hello()
	{
		return "this is the home page";
	}
	@GetMapping("/saveuser")
	public String saveUser(@RequestParam String username,@RequestParam String firstName,@RequestParam String lastName,@RequestParam int age,@RequestParam String password)
	{
		User user=new User(username,firstName,lastName,password,age);
		userService.saveMyUser(user);
		return "User saved";
	}
}
