package com.automate.controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.HashMap;

import javax.persistence.Table;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	@GetMapping("/getTables/{host}")
	public String get_tables(HttpServletRequest request,@RequestParam("host") String host) throws ScriptException, IOException, NoSuchMethodException
	{
		List<String> tb=userService.conn_db(host);
		request.setAttribute("tb", tb);
		request.setAttribute("host", host);
	/*	ScriptEngineManager manager=new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");
		engine.eval(Files.newBufferedReader(Paths.get("C:\\Users\\abhishek.i.b\\eclipse-workspace\\automateCG\\src\\main\\resources\\static\\disp_header.js"), StandardCharsets.UTF_8));
		Invocable inv = (Invocable) engine;
		inv.invokeFunction("display_toggle");   */
		return "index";
	}
	
	
	@GetMapping("/get_db/ {host}")
	public String get_db(HttpServletRequest request,@PathVariable String host)
	{
		return "test";
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
	
	String tables="";
	
	@GetMapping("/selectedSchema/{tbs}/{host}")
	public String selectedSchema(HttpServletRequest request,@PathVariable String tbs,@PathVariable String host)
	{
		tables=tbs;
		request.setAttribute("list", userService.show_tables(tbs,host));
		request.setAttribute("name", tbs);
		request.setAttribute("host", host);
		return "print_table";
	}
	
	@GetMapping("metadata/{l}/{name}/{host}")
	public String metadata(HttpServletRequest request,@PathVariable("l") String l,@PathVariable("name") String schema_name,@PathVariable("host") String host) throws SQLException
	{
		request.setAttribute("col_name", userService.get_column_name(l,schema_name,host));
		request.setAttribute("col_size", userService.get_col_size(l,schema_name,host));
		request.setAttribute("nullable", userService.get_nullable(l,schema_name,host));
		request.setAttribute("auto_incr", userService.get_AI(l,schema_name,host));
		request.setAttribute("pk", userService.get_PK(l,schema_name,host));
		request.setAttribute("pkName", userService.get_PK_name(l,schema_name,host));
		request.setAttribute("fk", userService.get_FK(l,schema_name,host));
		request.setAttribute("list", userService.show_tables(schema_name,host));
		//request.setAttribute("list", userService.show_tables(schema_name,host));
		return "tblProp";
	}
	
	List<String> l2=new ArrayList<String>();
	
	@GetMapping("/all_tables/{name}/{host}")
	public String all_tables(HttpServletRequest request,@PathVariable("host") String host,@PathVariable("name") String schema_name) throws SQLException
	{
	/*	request.setAttribute("col_name", userService.get_column_name());
		request.setAttribute("col_size", userService.get_col_size());
		request.setAttribute("nullable", userService.get_nullable());
		request.setAttribute("auto_incr", userService.get_AI());
		request.setAttribute("pk", userService.get_PK());
		request.setAttribute("pkName", userService.get_PK_name());
		request.setAttribute("fk", userService.get_FK());
		request.setAttribute("pk_fk",userService.get_FK_PK());
		request.setAttribute("pk_table",userService.get_PK_table());*/
		
		String checked="";
		String getChecked[]=request.getParameterValues("check");
		int i,j=0;
		List<String> l=new ArrayList<String>();
		
		List<String>[] col_name = new ArrayList[getChecked.length];
		List<String>[] col_size = new ArrayList[getChecked.length];
		List<String>[] nullable = new ArrayList[getChecked.length];
		List<String>[] auto_incr = new ArrayList[getChecked.length];
		List<String>[] pk = new ArrayList[getChecked.length];
		List<String>[] pkName=new ArrayList[getChecked.length];
		List<String>[] fk=new ArrayList[getChecked.length];
		List<String>[] pk_fk=new ArrayList[getChecked.length];
		List<String>[] pk_table=new ArrayList[getChecked.length];
		List<String>[] table_name=new ArrayList[getChecked.length];
		
		//  JSONObject res = new JSONObject(); 
		List<JSONObject>[] table_Res=new ArrayList[getChecked.length];
		  
		 
		for(i=0;i<getChecked.length;i++)
		{
			checked=getChecked[i];
			/*
			 * res = userService.get_desc_values(checked); table_Res.add(res);
			 */
			
		/*	  col_name[j]=userService.get_column_name(checked);
			  col_size[j]=userService.get_col_size(checked);
			  nullable[j]=userService.get_nullable(checked);
			  auto_incr[j]=userService.get_AI(checked); pk[j]=userService.get_PK(checked);
			  pkName[j]=userService.get_PK_name(checked);
			  fk[j]=userService.get_FK(checked); pk_fk[j]=userService.get_FK_PK(checked);
			  pk_table[j]=userService.get_PK_table(checked);
			  
			  int len=col_name[j].size(); List<String> l=new ArrayList<String>(); for(int
			  k=0;k<len;k++) { l.add(checked); }
			  
			  table_name[j]=l;
			 
			j++;	*/		
			
			l.add(checked);
			l2.add(checked);
			
			List<JSONObject> js = new ArrayList<JSONObject>();
			js=userService.get_desc_values(checked,host,schema_name);
			table_Res[i]=js;
			 System.out.println("table_Res  "+table_Res);
			 System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		
		}
		
	/*	request.setAttribute("col_name", col_name);
		request.setAttribute("col_size", col_size);
		request.setAttribute("nullable", nullable);
		request.setAttribute("auto_incr", auto_incr);
		request.setAttribute("pk", pk);
		request.setAttribute("pkName", pkName);
		request.setAttribute("fk", fk);
		request.setAttribute("pk_fk",pk_fk);
		request.setAttribute("pk_table",pk_table);
		request.setAttribute("table_name", table_name); */
		request.setAttribute("table_data", table_Res);
		request.setAttribute("nameList", l);
		request.setAttribute("schema_name", schema_name);
		request.setAttribute("host", host);
		return "all_tables";
	}
	
	@PostMapping("/show_info_schema")
	public String info_schema(HttpServletRequest request)
	{
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		jsonList=userService.get_info_schema();
		request.setAttribute("jsonList", jsonList);
		return "test";
	}
	
	@PostMapping("/generate/{schema_name}/{host}")
	public String generate(HttpServletRequest request,@PathVariable("schema_name") String schema_name,@PathVariable("host") String host)
	{
		request.setAttribute("schema_name", schema_name);
		request.setAttribute("host", host);
		request.setAttribute("nameList", l2);
		String password="";
		if(host.equals("localhost"))
			password="Sairam@27";
		else if(host.equals("10.16.70.147"))
			password="password";
		request.setAttribute("password", password);
		return "test";
	}
	
	
}
