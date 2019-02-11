package com.automate.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.persistence.Table;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.automate.services.SyncPipe;
import com.automate.services.UserService;

@Controller
public class ApplicationController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/getTables")
	public String get_tables(HttpServletRequest request)
	{
		
		System.out.print(userService.get_tables());
		return "index";
	}
	
	@RequestMapping("/exec")
	public String exec_cmd(HttpServletRequest request)
	{
		
		String command[]= { "cmd" };
		
		Process p;
		try
		{
			p=Runtime.getRuntime().exec(command);
			
			new Thread(new SyncPipe(p.getErrorStream(),System.err)).start();
			new Thread(new SyncPipe(p.getInputStream(),System.out)).start();
			
			PrintWriter stdin=new PrintWriter(p.getOutputStream());
			//stdin.println("mvn clean install com.jaxio.celerio:bootstrap-maven-plugin:bootstrap");
			stdin.println("start calc");
			stdin.close();
			p.waitFor();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return "index";
	}

}
