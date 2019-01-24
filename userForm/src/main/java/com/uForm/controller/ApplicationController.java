package com.uForm.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uForm.modal.User;
import com.uForm.services.UserService;

@Controller
public class ApplicationController {
	
	@Autowired
	private UserService userService;
	@RequestMapping("/welcome")
	public String Welcome(HttpServletRequest request)
	{
		request.setAttribute("mode","MODE_HOME");
		return "welcomepage";
	}
	
	@RequestMapping("/register")
	public String registration(HttpServletRequest request)
	{
		request.setAttribute("mode","MODE_REGISTER");
		return "welcomepage";
	}
	
	@PostMapping("/save-user")
	public String registerUser(@ModelAttribute User user,BindingResult bindingResult,HttpServletRequest request)
	{
		userService.saveMyUser(user);
		request.setAttribute("mode","MODE_HOME");
		return "welcomepage";
	}
	
	@GetMapping("/show-users")
	public String showAllUsers(HttpServletRequest request)
	{
		request.setAttribute("mode", "ALL_USERS");
		request.setAttribute("users", userService.showAllUsers());
		return "welcomepage";
	}
	
	@RequestMapping("/delete-user")
	public  String deleteUser(@RequestParam int id,HttpServletRequest request)
	{
		userService.deleteMyUser(id);
		return "welcomepage";
	}
	
	@RequestMapping("/edit-user")
	public String editUser(@RequestParam int id,HttpServletRequest request)
	{
		request.setAttribute("mode", "MODE_UPDATE");
		request.setAttribute("user", userService.editUser(id));
		
		return "welcomepage";
	}
	
	@RequestMapping("/login")
	public String login(HttpServletRequest request)
	{
		request.setAttribute("mode", "MODE_LOGIN");
		return "welcomepage";
	}
	
	
	
	@RequestMapping("/login-user")
	public String loginUser(@ModelAttribute User user,HttpServletRequest request)
	{
		if(userService.findByUsernameAndPassword(user.getUsername(), user.getPassword())!=null)
		{
			//request.setAttribute("mode", "MODE_LOGGEDIN");
			//request.setAttribute("user", userService.editUser(id));
			
		for(User u1:userService.showAllUsers())
			{
				if((u1.getUsername().equals(user.getUsername())) && (u1.getPassword().equals(user.getPassword())))
				{
					request.setAttribute("iD", u1.getId());
					request.setAttribute("name", u1.getUsername());
					request.setAttribute("firstname", u1.getFirstname());
					request.setAttribute("lastname", u1.getLastname());
					request.setAttribute("age", u1.getAge());
					request.setAttribute("password", user.getPassword());
				}
			}  
				
		    return "homepage";
		}
			
		else
		{
			request.setAttribute("error", "Invalid username and password");
			request.setAttribute("mode", "MODE_LOGIN");
			return "homepage";
		}
	}
	
/*	@GetMapping("/login-user/session")
	public String sessionValues(Model model,HttpSession session)
	{
		@SuppressWarnings("unchecked")
		List<String> values = (List<String>)session.getAttribute("iD");
		if(values==null)
			values=new ArrayList<>();
		model.addAttribute("val",values);
		return "homepage";
	}   */
	
}
