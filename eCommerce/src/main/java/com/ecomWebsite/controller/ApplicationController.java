package com.eComm.controller;

import java.util.Random;

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

import com.eComm.modal.User;
import com.eComm.modal.organization;
import com.eComm.services.UserService;

@Controller
public class ApplicationController {

	@Autowired
	private UserService userService;
	
	@RequestMapping("/index")
	public String showIndex(HttpServletRequest request)
	{
		
		return "index";
	}
	
	@GetMapping("/signup")
	public String showSignUpForm(organization org)
	{
		return "add-org";
	}  
	
	@GetMapping("/save-user")
	public String showsignUser()
	{
		return "saveuser";
	}
/*	@PostMapping("/addorg")
	public String addOrganization(@Valid organization organ,BindingResult result,Model model)
	{
		if(result.hasErrors())
			return "add-org";
		model.addAttribute("organ",organ);
		userService.saveOrg(organ);
		return "index";
	}   */
	@GetMapping("/prod")
	public String addprod()
	{
		return "add-prod";
	}
	
	@PostMapping("/addorg")
	public String addOrganization(@ModelAttribute organization org,Model model,HttpServletRequest request)
	{
		Random r=new Random();
		int x=r.nextInt(500);
		String name=org.getName();
		String country=org.getCountry();
		int n_emp=org.getNo_of_emps();
		String off_add=org.getOfficeaddress();
		String ph_no=org.getPh_no();
		float profits=org.getProfits();
		String web_url=org.getWebsiteurl();
		String code=name+"_0"+""+x;
		
		userService.savOrganization(code, name, country, ph_no, n_emp, profits, off_add, web_url);
		request.setAttribute("success", "Organization saved");
		return "orgSave";
	}
	
	
	
	@PostMapping("/sUser")
	public String saveUser(@ModelAttribute User user,Model model,HttpServletRequest request)
	{
		String name=user.getName();
		String email=user.getEmail();
		String org=user.getOrganization();
		String ph_no=user.getPh_no();
		String username=user.getUsername();
		String domain=user.getDomain();
		String password=user.getPassword();
		
		int id=userService.findtheId(org);
	/*	if(id==0)
		{
			request.setAttribute("status", "id not found");
			return "disp";
		}
		else
		{
			userService.saveUser(id, org, name, username, email, password, ph_no, domain);
			request.setAttribute("status", "User saved");
			return "disp";
		}  */
		
		userService.saveUser(id, org, name, username, email, password, ph_no, domain);
		request.setAttribute("status", "User saved");
		return "disp";
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

