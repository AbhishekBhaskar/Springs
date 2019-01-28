package com.ecomWebsite.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.ecomWebsite.modal.organization;
import com.ecomWebsite.services.UserService;

@Controller
public class ApplicationController {

	@Autowired
	private UserService userService;
	@PostMapping("/saveorg")
	public String registerOrg(@ModelAttribute organization org,BindingResult bindingRequest,HttpServletRequest request)
	{
		userService.saveOrg(org);
		
		return "org";
	}
}
