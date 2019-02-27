package com.automate.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.automate.modal.Database;
import com.automate.modal.appPack;
import com.automate.services.SyncPipe;
import com.automate.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class ApplicationController {

	@Autowired
	private UserService userService;
	// private static ObjectMapper mapper;

	List<String> l2 = new ArrayList<String>();

	@RequestMapping("index")
	public String showIndex(HttpServletRequest request) {
		l2.clear();
		return "index";
	}

	@GetMapping("/getTables/{host_ip}")
	public String get_tables(HttpServletRequest request, @RequestParam("host_ip") String host,
			@ModelAttribute Database db) throws ScriptException, IOException, NoSuchMethodException {
		String password = db.getPassword();
		String uname = db.getUsername();
		String remotedbname = db.getRegistered_name();
		request.getSession().setAttribute("uname", uname);
		request.getSession().setAttribute("password", password);
		List<String> tb = userService.conn_db(host, uname, password);
		request.setAttribute("tb", tb);
		request.setAttribute("host", host);
		String ip = userService.findIp(host);
		Random r = new Random();
		int x = r.nextInt(500);
		int y = r.nextInt(1000);
		if (ip == null) {
			userService.savDb(remotedbname, host, uname, password, x, y);
		}
		request.getSession().setAttribute("appid", x);
		request.getSession().setAttribute("orgid", y);
		/*
		 * ScriptEngineManager manager=new ScriptEngineManager(); ScriptEngine engine =
		 * manager.getEngineByName("JavaScript");
		 * engine.eval(Files.newBufferedReader(Paths.get(
		 * "C:\\Users\\abhishek.i.b\\eclipse-workspace\\automateCG\\src\\main\\resources\\static\\disp_header.js"
		 * ), StandardCharsets.UTF_8)); Invocable inv = (Invocable) engine;
		 * inv.invokeFunction("display_toggle");
		 */
		return "index";
	}

	@GetMapping("/get_db/ {host}")
	public String get_db(HttpServletRequest request, @PathVariable String host) {
		return "test";
	}

	@RequestMapping("/exec")
	public String exec_cmd(HttpServletRequest request) {

		String command[] = { "cmd" };

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);

			new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
			new Thread(new SyncPipe(p.getInputStream(), System.out)).start();

			PrintWriter stdin = new PrintWriter(p.getOutputStream());
			// stdin.println("mvn clean install
			// com.jaxio.celerio:bootstrap-maven-plugin:bootstrap");
			stdin.println("start calc");
			stdin.close();
			p.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "index";
	}

	@GetMapping("/disp_tables")
	public String btn_action(HttpServletRequest request) {
		/*
		 * ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		 * try { engine.eval(new FileReader("disp_header.js")); Invocable
		 * invocable=(Invocable)engine; } catch(ScriptException | FileNotFoundException
		 * e) { e.printStackTrace(); }
		 */

		request.setAttribute("users", userService.vendor_org_vals());
		request.setAttribute("orgs", userService.org_values());
		return "viewTableData";
	}

	String tables = "";

	@GetMapping("/selectedSchema/{tbs}/{host}")
	public String selectedSchema(HttpServletRequest request, @PathVariable String tbs, @PathVariable String host) {
		tables = tbs;
		request.setAttribute("list", userService.show_tables(tbs, host));
		request.setAttribute("name", tbs);
		request.setAttribute("host", host);
		return "print_table";
	}

	@GetMapping("metadata/{l}/{name}/{host}")
	public String metadata(HttpServletRequest request, @PathVariable("l") String l,
			@PathVariable("name") String schema_name, @PathVariable("host") String host) throws SQLException {
		request.setAttribute("col_name", userService.get_column_name(l, schema_name, host));
		request.setAttribute("col_size", userService.get_col_size(l, schema_name, host));
		request.setAttribute("nullable", userService.get_nullable(l, schema_name, host));
		request.setAttribute("auto_incr", userService.get_AI(l, schema_name, host));
		request.setAttribute("pk", userService.get_PK(l, schema_name, host));
		request.setAttribute("pkName", userService.get_PK_name(l, schema_name, host));
		request.setAttribute("fk", userService.get_FK(l, schema_name, host));
		request.setAttribute("list", userService.show_tables(schema_name, host));
		// request.setAttribute("list", userService.show_tables(schema_name,host));
		return "tblProp";
	}

	@GetMapping("/all_tables/{name}/{host}")
	public String all_tables(HttpServletRequest request, @PathVariable("host") String host,
			@PathVariable("name") String schema_name) throws SQLException {
		/*
		 * request.setAttribute("col_name", userService.get_column_name());
		 * request.setAttribute("col_size", userService.get_col_size());
		 * request.setAttribute("nullable", userService.get_nullable());
		 * request.setAttribute("auto_incr", userService.get_AI());
		 * request.setAttribute("pk", userService.get_PK());
		 * request.setAttribute("pkName", userService.get_PK_name());
		 * request.setAttribute("fk", userService.get_FK());
		 * request.setAttribute("pk_fk",userService.get_FK_PK());
		 * request.setAttribute("pk_table",userService.get_PK_table());
		 */

		String checked = "";
		String getChecked[] = request.getParameterValues("check");
		int i, j = 0;
		List<String> l = new ArrayList<String>();

		List<String>[] col_name = new ArrayList[getChecked.length];
		List<String>[] col_size = new ArrayList[getChecked.length];
		List<String>[] nullable = new ArrayList[getChecked.length];
		List<String>[] auto_incr = new ArrayList[getChecked.length];
		List<String>[] pk = new ArrayList[getChecked.length];
		List<String>[] pkName = new ArrayList[getChecked.length];
		List<String>[] fk = new ArrayList[getChecked.length];
		List<String>[] pk_fk = new ArrayList[getChecked.length];
		List<String>[] pk_table = new ArrayList[getChecked.length];
		List<String>[] table_name = new ArrayList[getChecked.length];

		// JSONObject res = new JSONObject();
		List<JSONObject>[] table_Res = new ArrayList[getChecked.length];

		for (i = 0; i < getChecked.length; i++) {
			checked = getChecked[i];
			/*
			 * res = userService.get_desc_values(checked); table_Res.add(res);
			 */

			/*
			 * col_name[j]=userService.get_column_name(checked);
			 * col_size[j]=userService.get_col_size(checked);
			 * nullable[j]=userService.get_nullable(checked);
			 * auto_incr[j]=userService.get_AI(checked); pk[j]=userService.get_PK(checked);
			 * pkName[j]=userService.get_PK_name(checked);
			 * fk[j]=userService.get_FK(checked); pk_fk[j]=userService.get_FK_PK(checked);
			 * pk_table[j]=userService.get_PK_table(checked);
			 * 
			 * int len=col_name[j].size(); List<String> l=new ArrayList<String>(); for(int
			 * k=0;k<len;k++) { l.add(checked); }
			 * 
			 * table_name[j]=l;
			 * 
			 * j++;
			 */

			l.add(checked);
			l2.add(checked);

			List<JSONObject> js = new ArrayList<JSONObject>();
			js = userService.get_desc_values(checked, host, schema_name);
			table_Res[i] = js;
			System.out.println("table_Res  " + table_Res);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

		}

		/*
		 * request.setAttribute("col_name", col_name); request.setAttribute("col_size",
		 * col_size); request.setAttribute("nullable", nullable);
		 * request.setAttribute("auto_incr", auto_incr); request.setAttribute("pk", pk);
		 * request.setAttribute("pkName", pkName); request.setAttribute("fk", fk);
		 * request.setAttribute("pk_fk",pk_fk);
		 * request.setAttribute("pk_table",pk_table); request.setAttribute("table_name",
		 * table_name);
		 */
		request.setAttribute("table_data", table_Res);
		request.setAttribute("nameList", l);
		request.setAttribute("schema_name", schema_name);
		request.setAttribute("host", host);
		return "all_tables";
	}

	@PostMapping("/show_info_schema")
	public String info_schema(HttpServletRequest request) {
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		jsonList = userService.get_info_schema();
		request.setAttribute("jsonList", jsonList);
		return "test";
	}

	@PostMapping("/generate/{schema_name}/{host}")
	public String generate(HttpServletRequest request,@PathVariable("schema_name") String schema_name,@PathVariable("host") String host,@ModelAttribute appPack ap) throws IOException
	{
		request.setAttribute("schema_name", schema_name);
		request.setAttribute("host", host);
		request.setAttribute("nameList", l2);
		String password="";
		if(host.equals("localhost"))
			password="Sairam@27";
		else if(host.equals("10.16.35.127"))
			password="password";
		request.setAttribute("password", password);
		
		
		DateFormat df1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//DateFormat df2=new SimpleDateFormat("HH:mm:ss");
		Date date=new Date();
		String finalDate=df1.format(date);
		//String finalTime=df2.format(date);
		request.setAttribute("date", finalDate);
		//request.setAttribute("time", finalTime);
		String finalName="";
		for(String name:l2)
		{
			finalName=finalName+name+",";
		}
		
		
		int x=(int) request.getSession().getAttribute("appid");
		int y=(int) request.getSession().getAttribute("orgid");
		String app_name=ap.getApp_name();
		String package_name=ap.getPackage_name();
		
		request.getSession().setAttribute("app_name", app_name);
		request.getSession().setAttribute("package_name", package_name);
		
		userService.save_transaction(host, schema_name, finalName, finalDate,"C:\\Users\\abhishek.i.b\\Desktop\\abhi\\springs",app_name,package_name,x,y);
		
		/* *******************************************  */
		/* replacing */
		
		String link="C:\\AutoCodeGeneration\\ivu.apie.database\\ivu.apie.database\\src\\main\\resources\\celerio\\pack-backend-jpa\\bootstrap\\pom.boot.vm.xml";
		String new_dbname="<database.name>" + schema_name + "</database.name>";
		String new_dbhost="<database.host>" + host + "</database.host>";
		String uname = (String) request.getSession().getAttribute("uname");
		String new_dbuser="<database.user>" + uname + "</database.host>";
		String passwd = (String) request.getSession().getAttribute("password");
		String new_dbpwd="<database.password>" + passwd + "</database.password>";
		String new_jdbc_url="<jdbc.url>jdbc:mysql://" + host + ":3306/" + schema_name + "</jdbc.user>";
		String new_jdbc_user="<jdbc.user>" + uname + "</jdbc.user>";
		String new_jdbc_passwd="<jdbc.password>" + passwd + "</jdbc.password>";
		
		String old_dbname="<database\\.name>.*";
		String old_dbhost="<database\\.host>.*";
		String old_dbuser="<database\\.user>.*";
		String old_dbpassword="<database\\.password>.*";		
		String old_jdbc_url="<jdbc\\.url>.*";
		String old_jdbc_user="<jdbc\\.user>.*";
		String old_jdbc_password="<jdbc\\.password>.*";
		
		String link2 = "C:\\AutoCodeGeneration\\ivu.apie.database\\ivu.apie.database\\src\\main\\resources\\celerio\\pack-backend-jpa\\src\\main\\java\\Application.p.vm.java";
		String old_pack_name1 = "@ComponentScan.*";
		String new_pack_name1 = "@ComponentScan({\"" + package_name + "\"})";
		
		String old_pack_name2 = "@EntityScan.*";
		String new_pack_name2 = "@EntityScan(\"" + package_name + "\")";
		
		String old_pack_name3 = "@EnableJpaRepositories.*";
		String new_pack_name3 = "@EnableJpaRepositories(\"" + package_name + "\")";
		
		userService.replace(link, new_dbname, old_dbname);
		userService.replace(link, new_dbhost, old_dbhost);
		userService.replace(link, new_dbuser, old_dbuser);
		userService.replace(link, new_dbpwd, old_dbpassword);
		userService.replace(link, new_jdbc_url, old_jdbc_url);
		userService.replace(link, new_jdbc_user, old_jdbc_user);
		userService.replace(link, new_jdbc_passwd, old_jdbc_password);
		userService.replace(link2, new_pack_name1, old_pack_name1);
		userService.replace(link2, new_pack_name2, old_pack_name2);
		userService.replace(link2, new_pack_name3, old_pack_name3);
		
	//	int timeToWait=25;
		
		
		/*
		 * try { Thread.sleep(10000); File f2=new File(
		 * "C:\\AutoCodeGeneration\\ivu.apie.database\\ivu.apie.database\\"+app_name+"\\
		 * pom.xml"); if(f2.exists()==true) { String link3= String link3 =
		 * "C:\\AutoCodeGeneration\\ivu.apie.database\\ivu.apie.database\\"+app_name+"\\
		 * pom.xml"; userService.replace(link3, new_dbname, old_dbname);
		 * userService.replace(link3, new_dbhost, old_dbhoat);
		 * userService.replace(link3, new_dbuser, old_dbuser);
		 * userService.replace(link3, new_dbpwd, old_dbpassword);
		 * 
		 * String newjdbcType = "<jdbc.type>mysql</jdbc.type>"; String oldjdbcType =
		 * "<jdbc\\.type>.*"; String newjdbcgid = "<jdbc.groupId>mysql</jdbc.groupId>";
		 * String oldjdbcgid = "<jdbc\\.groupId>.*"; String newjdbcartid =
		 * "<jdbc.artifactId>mysql-connector-java</jdbc.artifactId>"; String
		 * oldjdbcartid = "<jdbc\\.artifactId>.*"; String newjdbcDriver =
		 * "<jdbc.driver>com.mysql.jdbc.Driver</jdbc.driver>"; String oldjdbcDriver =
		 * "<jdbc\\.driver>.*"; String newhibernateD =
		 * "<hibernate.dialect>org.hibernate.dialect.MySQL5Dialect</hibernate.dialect>";
		 * String oldhibernateD = "<hibernate\\.dialect.*>"; //jdbc.url is already there
		 * 
		 * userService.replace(link3, newjdbcType, oldjdbcType);
		 * userService.replace(link3, newjdbcgid, oldjdbcgid);
		 * userService.replace(link3, newjdbcartid, oldjdbcartid);
		 * userService.replace(link3, newjdbcDriver, oldjdbcDriver);
		 * userService.replace(link3, new_jdbc_url, old_jdbc_url);
		 * userService.replace(link3, new_jdbc_user, old_jdbc_user);
		 * userService.replace(link3, new_jdbc_passwd, old_jdbc_password);
		 * userService.replace(link3, newhibernateD, oldhibernateD); } } catch(Exception
		 * e) { e.printStackTrace(); }
		 */
		 
		/* *******************************************  */
		
		
		/* *******************************************  */
		/* executing command */
		
		String line3;
		

		Process process = Runtime.getRuntime ().exec("cmd.exe /c mvn clean install com.jaxio.celerio:bootstrap-maven-plugin:bootstrap",null,
				new File("C:\\AutoCodeGeneration\\ivu.apie.database\\ivu.apie.database"));
		OutputStream stdin = process.getOutputStream ();
		InputStream stderr = process.getErrorStream ();
		InputStream stdout = process.getInputStream ();

		BufferedReader reader3 = new BufferedReader (new InputStreamReader(stdout));
		BufferedWriter writer3 = new BufferedWriter(new OutputStreamWriter(stdin));

		String input = "2";//scan.nextLine();
		input += "\n";
		writer3.write(input);
		writer3.flush();

		input = "4";
		input += "\n";
		writer3.write(input);
		writer3.flush();  

		/*
		 * while ((line = reader.readLine ()) != null) { System.out.println ("Stdout: "
		 * + line); }
		 */

		input = app_name;
		//input = "vvvvvv";
		input += "\n";
		writer3.write(input);
		writer3.flush();
		
		input = package_name;
		//input = "package_name";
		input += "\n";
		writer3.write(input);
		writer3.close();

		while ((line3 = reader3.readLine ()) != null) {
		System.out.println ("Stdout: " + line3);
		}   
	
		
		/* *******************************************  */
		
		
		return "test";
	}

	static void copy(InputStream in, OutputStream out) throws IOException {
		while (true) {
			int c = in.read();
			if (c == -1)
				break;
			out.write((char) c);
		}
	}

	@PostMapping("/finishExec/{schema_name}/{host}")
	public String finishExec(HttpServletRequest request, @PathVariable("schema_name") String schema_name,@PathVariable("host") String host)
	{
		String app_name = (String) request.getSession().getAttribute("app_name");
		String package_name = (String) request.getSession().getAttribute("package_name");
		String link3 = "C:\\AutoCodeGeneration\\ivu.apie.database\\ivu.apie.database\\" + app_name + "\\pom.xml";

		String new_dbname = "<database.name>" + schema_name + "</database.name>";
		String new_dbhost = "<database.host>" + host + "</database.host>";
		String uname = (String) request.getSession().getAttribute("uname");
		String new_dbuser = "<database.user>" + uname + "</database.host>";
		String passwd = (String) request.getSession().getAttribute("password");
		String new_dbpwd = "<database.password>" + passwd + "</database.password>";
		String new_jdbc_url = "<jdbc.url>jdbc:mysql://" + host + ":3306/" + schema_name + "</jdbc.user>";
		String new_jdbc_user = "<jdbc.user>" + uname + "</jdbc.user>";
		String new_jdbc_passwd = "<jdbc.password>" + passwd + "</jdbc.password>";

		String old_dbname = "<database\\.name>.*";
		String old_dbhost = "<database\\.host>.*";
		String old_dbuser = "<database\\.user>.*";
		String old_dbpassword = "<database\\.password>.*";
		String old_jdbc_url = "<jdbc\\.url>.*";
		String old_jdbc_user = "<jdbc\\.user>.*";
		String old_jdbc_password = "<jdbc\\.password>.*";
		
		userService.replace(link3, new_dbname, old_dbname);
		userService.replace(link3, new_dbhost, old_dbhost);
		  userService.replace(link3, new_dbuser, old_dbuser);
		  userService.replace(link3, new_dbpwd, old_dbpassword);

		String newjdbcType = "<jdbc.type>mysql</jdbc.type>";
		String oldjdbcType = "<jdbc\\.type>.*";
		String newjdbcgid = "<jdbc.groupId>mysql</jdbc.groupId>";
		String oldjdbcgid = "<jdbc\\.groupId>.*";
		String newjdbcartid = "<jdbc.artifactId>mysql-connector-java</jdbc.artifactId>";
		String oldjdbcartid = "<jdbc\\.artifactId>.*";
		String newjdbcDriver = "<jdbc.driver>com.mysql.jdbc.Driver</jdbc.driver>";
		String oldjdbcDriver = "<jdbc\\.driver>.*";
		String newhibernateD = "<hibernate.dialect>org.hibernate.dialect.MySQL5Dialect</hibernate.dialect>";
		String oldhibernateD = "<hibernate\\.dialect.*>";
		
		userService.replace(link3, newjdbcType, oldjdbcType);
		  userService.replace(link3, newjdbcgid, oldjdbcgid);
		  userService.replace(link3, newjdbcartid, oldjdbcartid);
		  userService.replace(link3, newjdbcDriver, oldjdbcDriver);
		  userService.replace(link3, new_jdbc_url, old_jdbc_url);
		  userService.replace(link3, new_jdbc_user, old_jdbc_user);
		  userService.replace(link3, new_jdbc_passwd, old_jdbc_password);
		  userService.replace(link3, newhibernateD, oldhibernateD);  
		  
		  String link4 = "C:\\AutoCodeGeneration\\ivu.apie.database\\ivu.apie.database\\" + app_name + "\\src\\main\\config\\celerio-maven-plugin\\celerio-maven-plugin.xml";
		  String newpattern = "pattern=organization";
		  String oldpattern = "pattern=.*";
		  userService.replace(link4, newpattern, oldpattern); 
		  request.setAttribute("done", "Code generated");
		return "test";
	}
}
