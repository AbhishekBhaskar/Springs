package com.CodeGenAPI.Controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.script.ScriptException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.CodeGenAPI.Modal.Database;
import com.CodeGenAPI.Modal.appPack;
import com.CodeGenAPI.Services.SyncPipe;
import com.CodeGenAPI.Services.CelerioService;

//@Controller
public class CelerioController {

	@Autowired
	private CelerioService userService;
	

	List<String> l2 = new ArrayList<String>();
	List<String> fileList = new ArrayList<String>();

	@RequestMapping("index")
	public String showIndex(HttpServletRequest request) {
		l2.clear();
		fileList.clear();
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

	

	String tables = "";

	@GetMapping("/selectedSchema/{tbs}/{host}")
	public String selectedSchema(HttpServletRequest request, @PathVariable String tbs, @PathVariable String host) {
		tables = tbs;
		String uname = (String) request.getSession().getAttribute("uname");
		String passwd = (String) request.getSession().getAttribute("password");

		request.setAttribute("list", userService.show_tables(tbs, host, uname, passwd));
		request.setAttribute("name", tbs);
		request.setAttribute("host", host);
		return "print_table";
	}

	

	@GetMapping("/all_tables/{name}/{host}")
	public String all_tables(HttpServletRequest request, @PathVariable("host") String host,
			@PathVariable("name") String schema_name) throws SQLException {
		
		l2.clear();
		String checked = "";
		String getChecked[] = request.getParameterValues("check");
		int i, j = 0;
		List<String> l = new ArrayList<String>();

		

		String uname = (String) request.getSession().getAttribute("uname");
		String passwd = (String) request.getSession().getAttribute("password");
		// JSONObject res = new JSONObject();
		List<JSONObject>[] table_Res = new ArrayList[getChecked.length];

		for (i = 0; i < getChecked.length; i++) {
			checked = getChecked[i];
			

			l.add(checked);
			l2.add(checked);

			List<JSONObject> js = new ArrayList<JSONObject>();
			js = userService.get_desc_values(checked, host, schema_name, uname, passwd);
			table_Res[i] = js;
			System.out.println("table_Res  " + table_Res);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

		}

		
		request.setAttribute("table_data", table_Res);
		request.setAttribute("nameList", l2);
		request.setAttribute("schema_name", schema_name);
		request.setAttribute("host", host);
		return "all_tables";
	}

	@PostMapping("/generate/{schema_name}/{host}")
	public String generate(HttpServletRequest request, @PathVariable("schema_name") String schema_name, @PathVariable("host") String host, @ModelAttribute appPack ap) throws IOException, InterruptedException 
	{
		request.setAttribute("schema_name", schema_name);
		request.setAttribute("host", host);
		request.setAttribute("nameList", l2);
		String password = "";
		if (host.equals("localhost"))
			password = "Sairam@27";
		else if (host.equals("10.16.35.127"))
			password = "password";
		String passwd = (String) request.getSession().getAttribute("password");
		request.setAttribute("password", passwd);

		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// DateFormat df2=new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		String finalDate = df1.format(date);
		// String finalTime=df2.format(date);
		request.setAttribute("date", finalDate);
		// request.setAttribute("time", finalTime);
		String finalName = "";
		for (String name : l2) {
			finalName = finalName + name + ",";
		}

		int x = (int) request.getSession().getAttribute("appid");
		int y = (int) request.getSession().getAttribute("orgid");
		String app_name = ap.getApp_name();
		String package_name = ap.getPackage_name();

		request.getSession().setAttribute("app_name", app_name);
		request.getSession().setAttribute("package_name", package_name);

		userService.save_transaction(host, schema_name, finalName, finalDate,
				"C:\\Users\\abhishek.i.b\\Desktop\\abhi\\springs", app_name, package_name, x, y);

		/* ******************************************* */
		/* replacing */

		String link = "C:\\AutoCodeGeneration\\ivu.apie.database\\ivu.apie.database\\src\\main\\resources\\celerio\\pack-backend-jpa\\bootstrap\\pom.boot.vm.xml";
		String new_dbname = "<database.name>" + schema_name + "</database.name>";
		String new_dbhost = "<database.host>" + host + "</database.host>";
		String uname = (String) request.getSession().getAttribute("uname");
		String new_dbuser = "<database.user>" + uname + "</database.user>";
		//String passwd = (String) request.getSession().getAttribute("password");
		String new_dbpwd = "<database.password>" + passwd + "</database.password>";
		String new_jdbc_url = "<jdbc.url>jdbc:mysql://" + host + ":3306/" + schema_name + "</jdbc.url>";
		String new_jdbc_comment = "";
		String new_jdbc_user = "<jdbc.user>" + uname + "</jdbc.user>";
		String new_jdbc_passwd = "<jdbc.password>" + passwd + "</jdbc.password>";

		String old_dbname = "<database\\.name>.*";
		String old_dbhost = "<database\\.host>.*";
		String old_dbuser = "<database\\.user>.*";
		String old_dbpassword = "<database\\.password>.*";
		String old_jdbc_url = "<jdbc\\.url>.*";
		String old_jdbc_comment = "<!-- <jdbc\\.url>.*";
		String old_jdbc_user = "<jdbc\\.user>.*";
		String old_jdbc_password = "<jdbc\\.password>.*";

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
		userService.replace(link, new_jdbc_comment, old_jdbc_comment);
		userService.replace(link, new_jdbc_url, old_jdbc_url);
		userService.replace(link, new_jdbc_user, old_jdbc_user);
		userService.replace(link, new_jdbc_passwd, old_jdbc_password);
		userService.replace(link2, new_pack_name1, old_pack_name1);
		userService.replace(link2, new_pack_name2, old_pack_name2);
		userService.replace(link2, new_pack_name3, old_pack_name3);

		

		/* ******************************************* */

		/* ******************************************* */
		/* executing command */

		String line3;

		Process process = Runtime.getRuntime().exec("cmd.exe /c mvn clean install com.jaxio.celerio:bootstrap-maven-plugin:bootstrap", null, new File("C:\\AutoCodeGeneration\\ivu.apie.database\\ivu.apie.database"));
		OutputStream stdin = process.getOutputStream();
		InputStream stderr = process.getErrorStream();
		InputStream stdout = process.getInputStream();

		BufferedReader reader3 = new BufferedReader(new InputStreamReader(stdout));
		BufferedWriter writer3 = new BufferedWriter(new OutputStreamWriter(stdin));

		String input = "2";// scan.nextLine();
		input += "\n";
		writer3.write(input);
		writer3.flush();

		input = "4";
		input += "\n";
		writer3.write(input);
		writer3.flush();

		

		input = app_name;
		// input = "vvvvvv";
		input += "\n";
		writer3.write(input);
		writer3.flush();

		input = package_name;
		// input = "package_name";
		input += "\n";
		writer3.write(input);
		writer3.close();

		while ((line3 = reader3.readLine()) != null) {
			System.out.println("Stdout: " + line3);
		}

		/* ******************************************* */
		
		String link3 = "C:\\AutoCodeGeneration\\ivu.apie.database\\ivu.apie.database\\" + app_name + "\\pom.xml";
		File f=new File(link3);
//		while(!f.exists())
//		{
//			delayProg();
//		}
		finishExec(request, schema_name, host);
		String link4 = "C:\\AutoCodeGeneration\\ivu.apie.database\\ivu.apie.database\\" + app_name + "\\src\\main\\generated-java\\com\\test\\Application.java";
		File f2 = new File(link4);
//		while(!f2.exists())
//			delayProg();
		//request.setAttribute("executed", "executed");
		request.setAttribute("done", "Code generated");
		return "test2";
	}

	static void copy(InputStream in, OutputStream out) throws IOException {
		while (true) {
			int c = in.read();
			if (c == -1)
				break;
			out.write((char) c);
		}
	}
	

	
	String generateZipEntry(String file,String link){
    	return file.substring(link.length()+1, file.length());
    }

	 public void generateFileList(File node,String link){

	    	//add file only
		if(node.isFile()){
			fileList.add(generateZipEntry(node.getAbsoluteFile().toString(),link));
		}
			
		if(node.isDirectory()){
			String[] subNote = node.list();
			for(String filename : subNote){
				generateFileList(new File(node, filename),link);
			}
		}
	 
	    }
	 
	

//	@PostMapping("/finishExec/{schema_name}/{host}")
//	public String finishExec(HttpServletRequest request, @PathVariable("schema_name") String schema_name, @PathVariable("host") String host) throws IOException {
	 public void finishExec(HttpServletRequest request, String schema_name, String host) throws IOException {
		String app_name = (String) request.getSession().getAttribute("app_name");
		String package_name = (String) request.getSession().getAttribute("package_name");
		String link3 = "C:\\AutoCodeGeneration\\ivu.apie.database\\ivu.apie.database\\" + app_name + "\\pom.xml";

		String new_dbname = "<database.name>" + schema_name + "</database.name>";
		String new_dbhost = "<database.host>" + host + "</database.host>";
		String uname = (String) request.getSession().getAttribute("uname");
		String new_dbuser = "<database.user>" + uname + "</database.user>";
		String passwd = (String) request.getSession().getAttribute("password");
		String new_dbpwd = "<database.password>" + passwd + "</database.password>";
		String new_jdbc_url = "<jdbc.url>jdbc:mysql://" + host + ":3306/" + schema_name + "</jdbc.url>";
		String new_jdbc_user = "<jdbc.user>" + uname + "</jdbc.user>";
		String new_jdbc_passwd = "<jdbc.password>" + passwd + "</jdbc.password>";
		String newgenJava = "java";
		String new_celerio_pack = "com.hm.ivu.apie"; 
		String new_pack_backend = "ivu.apie.database";
		String new_version = "1.0.0";

		String old_dbname = "<database\\.name>.*";
		String old_dbhost = "<database\\.host>.*";
		String old_dbuser = "<database\\.user>.*";
		String old_dbpassword = "<database\\.password>.*";
		String old_jdbc_url = "<jdbc\\.url>.*";
		String old_jdbc_user = "<jdbc\\.user>.*";
		String old_jdbc_password = "<jdbc\\.password>.*";
		String oldgenJava = "generated-java";
		String old_celerio_pack = "com.jaxio.celerio.packs";
		String old_pack_backend = "pack-backend-jpa";
		String old_version = "1.0.4";
		

		userService.replace(link3, new_dbname, old_dbname);
		userService.replace(link3, new_dbhost, old_dbhost);
		userService.replace(link3, new_dbuser, old_dbuser);
		userService.replace(link3, new_dbpwd, old_dbpassword);
		//userService.replace(link3, newgenJava, oldgenJava);
		userService.replace(link3, new_celerio_pack,old_celerio_pack);
		userService.replace(link3, new_pack_backend,old_pack_backend);
		userService.replace(link3, new_version,old_version);

		String newjdbcType = "<jdbc.type>mysql</jdbc.type>";
		String oldjdbcType = "<jdbc\\.type>.*";
		String newjdbcgid = "<jdbc.groupId>mysql</jdbc.groupId>";
		String oldjdbcgid = "<jdbc\\.groupId>.*";
		String newjdbcartid = "<jdbc.artifactId>mysql-connector-java</jdbc.artifactId>";
		String oldjdbcartid = "<jdbc\\.artifactId>.*";
		String newversion = "<jdbc.version>5.1.25</jdbc.version>";
		String oldversion = "<jdbc\\.version>.*";
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
		userService.replace(link3, newversion, oldversion);
		

		String link4 = "C:\\AutoCodeGeneration\\ivu.apie.database\\ivu.apie.database\\" + app_name + "\\src\\main\\config\\celerio-maven-plugin\\celerio-maven-plugin.xml";
		String newpattern = "";
		for (String i : l2) {
			newpattern += "<table include=\"true\" pattern=\"" + i + "\"/>\n\t";
		}
		
		String oldpattern = "<table include=\"true\".*";
		userService.replace(link4, newpattern, oldpattern);

		String line3;
		Process process = Runtime.getRuntime().exec("cmd.exe /c mvn -Pmetadata,gen generate-sources", null, new File("C:\\AutoCodeGeneration\\ivu.apie.database\\ivu.apie.database\\" + app_name));
		OutputStream stdin = process.getOutputStream();
		InputStream stderr = process.getErrorStream();
		InputStream stdout = process.getInputStream();

		BufferedReader reader3 = new BufferedReader(new InputStreamReader(stdout));
		BufferedWriter writer3 = new BufferedWriter(new OutputStreamWriter(stdin));

		while ((line3 = reader3.readLine()) != null) {
			System.out.println("Stdout: " + line3);
		}
		
		
		
	/* *********************************************** */
		/* zipping */
		
		File f=new File("C:\\AutoCodeGeneration\\ivu.apie.database\\ivu.apie.database\\" + app_name);
		String link="C:\\AutoCodeGeneration\\ivu.apie.database\\ivu.apie.database\\" + app_name;
		generateFileList(f,link);
		
		
		String OUTPUT_ZIP_FILE = "C:\\AutoCodeGeneration\\ivu.apie.database\\ivu.apie.database\\"+app_name+".zip";
		String SOURCE_FOLDER = "C:\\AutoCodeGeneration\\ivu.apie.database\\ivu.apie.database\\"+app_name;
		String zip_name = app_name+".zip";

		byte[] buffer = new byte[1024];
		

		try {

			FileOutputStream fos = new FileOutputStream(OUTPUT_ZIP_FILE);
			ZipOutputStream zos = new ZipOutputStream(fos);

			

			for (String file : fileList) {

				
				ZipEntry ze = new ZipEntry(file);
				zos.putNextEntry(ze);

				FileInputStream in = new FileInputStream(SOURCE_FOLDER + File.separator + file);

				int len;
				while ((len = in.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}

				in.close();
			}

			zos.closeEntry();
			// remember close it
			zos.close();
			//fos.close();

			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	/* *************************************** */	

		request.setAttribute("done", "Code generated");
		request.setAttribute("download_link","C:\\AutoCodeGeneration\\ivu.apie.database\\ivu.apie.database\\" + app_name);
		request.setAttribute("app_name", app_name);
		request.setAttribute("zip_name", zip_name);
		//return "test2";
	}
	
	
	
	@PostMapping("/downloadCode/{zip_name}")
	public String downloadCode(HttpServletRequest request,HttpServletResponse response, @PathVariable("zip_name") String fileName) throws IOException
	{
		 
		String app_name = (String) request.getSession().getAttribute("app_name");
		
		String DIRECTORY = "C:\\AutoCodeGeneration\\ivu.apie.database\\ivu.apie.database";
		String DEFAULT_FILE_NAME = fileName;
		ServletContext servletContext;
		File file = new File(DIRECTORY + "/" + fileName);
		 response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());
		 response.setContentLength((int) file.length());
		 
		 BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
	     BufferedOutputStream outStream = new BufferedOutputStream(response.getOutputStream());
	     
	     byte[] buffer = new byte[1024];
	        int bytesRead = 0;
	        while ((bytesRead = inStream.read(buffer)) != -1) {
	            outStream.write(buffer, 0, bytesRead);
	        }
	        outStream.flush();
	        inStream.close();
	        outStream.close();
	     
		request.setAttribute("zipped", "Folder zipped");
		return "test2";
	}
	
	

}
