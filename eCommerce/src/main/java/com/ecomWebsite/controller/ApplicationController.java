package com.eComm.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.eComm.modal.organization;
import com.eComm.services.UserService;

@Controller
public class ApplicationController {

	@Autowired
	private UserService userService;
	@GetMapping("/signup")
	public String showSignUpForm(organization org)
	{
		return "add-org";
	}
	@PostMapping("/addorg")
	public String addOrganization(@Valid organization organ,BindingResult result,Model model)
	{
		if(result.hasErrors())
			return "add-org";
		userService.saveOrg(organ);
		return "index";
	}
	
	
	
/*	@GetMapping("/index")
	public ModelAndView createUser()
	{
		ModelAndView modelAndView=new ModelAndView("index");
		modelAndView.addObject("org", new organization());
		return modelAndView;
	}
	
	
	@PostMapping("/index")
	public String saveOrg(@Valid organization org,BindingResult bindingRequest)
	{
		//ModelAndView modelAndView=new ModelAndView();
		userService.saveOrg(org);
		return "/index";
	}  */
	
	
	/*public String registerOrg(@ModelAttribute organization org,BindingResult bindingRequest,HttpServletRequest request)
	{
		request.setAttribute("org", org);
		userService.saveOrg(org);
		
		return "index";
	} */  
}

