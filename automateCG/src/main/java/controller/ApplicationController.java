package com.automate.controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import javax.persistence.Table;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.automate.services.SyncPipe;
import com.automate.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class ApplicationController {
	
	@Autowired
	private UserService userService;
	//private static ObjectMapper mapper;
	
	
	

	
	
	
	@RequestMapping("index")
	public String showIndex(HttpServletRequest request)
	{
		return "index";
	}
	@PostMapping("/getTables")
	public String get_tables(HttpServletRequest request) throws ScriptException, IOException, NoSuchMethodException
	{
		List<String> tb=userService.conn_db();
		request.setAttribute("tb", tb);
	/*	ScriptEngineManager manager=new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");
		engine.eval(Files.newBufferedReader(Paths.get("C:\\Users\\abhishek.i.b\\eclipse-workspace\\automateCG\\src\\main\\resources\\static\\disp_header.js"), StandardCharsets.UTF_8));
		Invocable inv = (Invocable) engine;
		inv.invokeFunction("display_toggle");   */
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
	
	   
	
	@GetMapping("/disp_tables")
	public String btn_action(HttpServletRequest request)
	{
	/*		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		try
		{
			engine.eval(new FileReader("disp_header.js"));
			Invocable invocable=(Invocable)engine;
		}
		catch(ScriptException | FileNotFoundException e)
		{
			e.printStackTrace();
		}  */  
		
		
		request.setAttribute("users", userService.vendor_org_vals());
		request.setAttribute("orgs", userService.org_values());
		return "viewTableData";
	}
	
	@GetMapping("/selectedSchema")
	public String selectedSchema(HttpServletRequest request)
	{
		request.setAttribute("list", userService.show_tables());
		return "print_table";
	}
	
	@PostMapping("metadata")
	public String metadata(HttpServletRequest request) throws SQLException
	{
		request.setAttribute("col_name", userService.get_column_name());
		request.setAttribute("col_size", userService.get_col_size());
		request.setAttribute("nullable", userService.get_nullable());
		request.setAttribute("auto_incr", userService.get_AI());
		request.setAttribute("pk", userService.get_PK());
		request.setAttribute("pkName", userService.get_PK_name());
		request.setAttribute("fk", userService.get_FK());
		request.setAttribute("list", userService.show_tables());
		return "tblProp";
	}
}
