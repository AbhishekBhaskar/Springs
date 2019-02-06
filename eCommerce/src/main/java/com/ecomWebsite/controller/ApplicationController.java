package com.ecomWebsite.controller;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.persistence.criteria.Path;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecomWebsite.modal.User;
import com.ecomWebsite.modal.organization;
import com.ecomWebsite.modal.products;
import com.ecomWebsite.services.UserService;

@Controller
@SessionAttributes("orgid")
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
	
	@GetMapping("/prod")
	public String addprod()
	{
		return "add-prod";
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
	
	
	
	@PostMapping("/addProd")
	public String addProduct(@ModelAttribute products prod,Model model,HttpServletRequest request)
	{
		Random r=new Random();
		int x=r.nextInt(500);
		String name=prod.getName();
		String license_number=prod.getLicense_number();
		int warranty_period=prod.getWarranty_period();
		String pr_url=prod.getUrl();
		float cost_per_item=prod.getCost_per_item();
	//	byte[] image=prod.getImage();
		String code="PR_0"+""+x;
		int checkCode=userService.checkCode(code);
		if(checkCode==1)
		{
			x=x+1;
			code="PR_0"+""+x;
		}
		
		
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		String finalDate=df.format(date);
		request.setAttribute("date", df.format(date));
		request.setAttribute("code", checkCode);
		request.setAttribute("url", pr_url);
		int oid=(int) request.getSession().getAttribute("organizationId");
		request.setAttribute("oid", oid);
		userService.saveProd(oid, code, name, pr_url, license_number, cost_per_item, finalDate, warranty_period);
		
		return "prodSave";
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
	
	@PostMapping("/login-user")
	public String login(@ModelAttribute User user,HttpServletRequest request)
	{
		String username=user.getUsername();
		String password=user.getPassword();
		int id=userService.findloggedId(username, password);
		if(id!=0)
		{
			request.setAttribute("orgid", id);
			request.getSession().setAttribute("organizationId", id);
		}  
		
		
		return "add-prod";
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
