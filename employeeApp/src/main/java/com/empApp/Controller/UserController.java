package com.empApp.Controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import com.empApp.model.User;
import com.empApp.Services.UserService;
import com.empApp.Repository.UserRepository;

@Controller
public class UserController {
	@Autowired
	private UserService userService;

	@RequestMapping("/index")
	public String index(HttpServletRequest request) {
		return "index";
	}

	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute("User") User user, BindingResult result, Model model) {
		userService.saveUser(user);
		model.addAttribute("saved", "User saved");
		return "index";
	}
	
	@PostMapping("/showUsers")
	public String showAllUsers(Model model)
	{
		List<User> userList = new ArrayList<User>();
		userList = userService.showUsers();
		model.addAttribute("userList", userList);
		return "index";
	}
	
	@PostMapping("/showColDef")
	public String showColDef(Model model) throws ClassNotFoundException, SQLException
	{
		
		List<JSONObject> js = new ArrayList<JSONObject>();
		js = userService.getColumnDefinition();
		model.addAttribute("colInfo", js);
		return "index";
	}
	
	@PostMapping("/genPojo")
	public String genPojo(Model model) throws ClassNotFoundException, SQLException
	{
		List<JSONObject> js = new ArrayList<JSONObject>();
		js = userService.getColumnDefinition();
		userService.picoWriterMethod(js);
		return "index";
	}

}
