package com.CodeGenAPI.Controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.CodeGenAPI.Modal.CodeGenDetails;
import com.CodeGenAPI.Modal.Database;
import com.CodeGenAPI.Modal.HostUser;
import com.CodeGenAPI.Modal.RemoteDb;
import com.CodeGenAPI.Modal.SelectDatabase;
import com.CodeGenAPI.Modal.appPack;
import com.CodeGenAPI.Services.CelerioService;
import com.CodeGenAPI.Services.PicocogService;

@Controller
public class ApplicationController {
	@Autowired
	private CelerioService celerioService;
	@Autowired
	private PicocogService picocogService;
	
	List<String> celerioTableList = new ArrayList<String>();
	List<String> celerioFileList = new ArrayList<String>();
	
	List<String> picocogTableList = new ArrayList<String>();
	List<String> picocogFileList = new ArrayList<String>();
	
	@RequestMapping("index")
	public String showIndex(HttpServletRequest request) 
	{
		celerioTableList.clear();
		celerioFileList.clear();
		picocogFileList.clear();
		return "index";
	}
	
	@PostMapping("/selectProject")
	public String selectProject(Model model,@ModelAttribute SelectDatabase db)
	{
		String selectedDb = db.getSelectedDB();
		
		if(selectedDb.equals("mysql"))
			return "CelerioIndex";
		else
			return "picocogIndex";
			
	}
	
	@GetMapping("/getTables/{host_ip}")
	public String get_tables(HttpServletRequest request, @RequestParam("host_ip") String host,
			@ModelAttribute Database db) throws ScriptException, IOException, NoSuchMethodException {
		String password = db.getPassword();
		String uname = db.getUsername();
		String remotedbname = db.getRegistered_name();
		request.getSession().setAttribute("uname", uname);
		request.getSession().setAttribute("password", password);
		List<String> tb = celerioService.conn_db(host, uname, password);
		request.setAttribute("tb", tb);
		request.setAttribute("host", host);
		String ip = celerioService.findIp(host);
		Random r = new Random();
		int x = r.nextInt(500);
		int y = r.nextInt(1000);
		if (ip == null) {
			celerioService.savDb(remotedbname, host, uname, password, x, y);
		}
		request.getSession().setAttribute("appid", x);
		request.getSession().setAttribute("orgid", y);
		
		return "CelerioIndex";
	}
	
	String tables = "";

	@GetMapping("/selectedSchema/{tbs}/{host}")
	public String selectedSchema(HttpServletRequest request, @PathVariable String tbs, @PathVariable String host) {
		tables = tbs;
		String uname = (String) request.getSession().getAttribute("uname");
		String passwd = (String) request.getSession().getAttribute("password");

		request.setAttribute("list", celerioService.show_tables(tbs, host, uname, passwd));
		request.setAttribute("name", tbs);
		request.setAttribute("host", host);
		return "CelerioPrintTable";
	}
	
	@GetMapping("/all_tables/{name}/{host}")
	public String all_tables(HttpServletRequest request, @PathVariable("host") String host,
			@PathVariable("name") String schema_name) throws SQLException {
		
		celerioTableList.clear();
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
			celerioTableList.add(checked);

			List<JSONObject> js = new ArrayList<JSONObject>();
			js = celerioService.get_desc_values(checked, host, schema_name, uname, passwd);
			table_Res[i] = js;
			System.out.println("table_Res  " + table_Res);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

		}

		
		request.setAttribute("table_data", table_Res);
		request.setAttribute("nameList", celerioTableList);
		request.setAttribute("schema_name", schema_name);
		request.setAttribute("host", host);
		return "CelerioAllTables";
	}
	
	@PostMapping("/generate/{schema_name}/{host}")
	public String generate(HttpServletRequest request, @PathVariable("schema_name") String schema_name, @PathVariable("host") String host, @ModelAttribute appPack ap) throws IOException, InterruptedException 
	{
		request.setAttribute("schema_name", schema_name);
		request.setAttribute("host", host);
		request.setAttribute("nameList", celerioTableList);
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
		for (String name : celerioTableList) {
			finalName = finalName + name + ",";
		}

		int x = (int) request.getSession().getAttribute("appid");
		int y = (int) request.getSession().getAttribute("orgid");
		String app_name = ap.getApp_name();
		String package_name = ap.getPackage_name();

		request.getSession().setAttribute("app_name", app_name);
		request.getSession().setAttribute("package_name", package_name);

		celerioService.save_transaction(host, schema_name, finalName, finalDate,
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

		celerioService.replace(link, new_dbname, old_dbname);
		celerioService.replace(link, new_dbhost, old_dbhost);
		celerioService.replace(link, new_dbuser, old_dbuser);
		celerioService.replace(link, new_dbpwd, old_dbpassword);
		celerioService.replace(link, new_jdbc_comment, old_jdbc_comment);
		celerioService.replace(link, new_jdbc_url, old_jdbc_url);
		celerioService.replace(link, new_jdbc_user, old_jdbc_user);
		celerioService.replace(link, new_jdbc_passwd, old_jdbc_password);
		celerioService.replace(link2, new_pack_name1, old_pack_name1);
		celerioService.replace(link2, new_pack_name2, old_pack_name2);
		celerioService.replace(link2, new_pack_name3, old_pack_name3);

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

		finishExec(request, schema_name, host);
		String link4 = "C:\\AutoCodeGeneration\\ivu.apie.database\\ivu.apie.database\\" + app_name + "\\src\\main\\generated-java\\com\\test\\Application.java";
		File f2 = new File(link4);
		request.setAttribute("done", "Code generated");
		return "codeGenerated";
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
			celerioFileList.add(generateZipEntry(node.getAbsoluteFile().toString(),link));
		}
			
		if(node.isDirectory()){
			String[] subNote = node.list();
			for(String filename : subNote){
				generateFileList(new File(node, filename),link);
			}
		}
	 
	    }
	 
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
			

			celerioService.replace(link3, new_dbname, old_dbname);
			celerioService.replace(link3, new_dbhost, old_dbhost);
			celerioService.replace(link3, new_dbuser, old_dbuser);
			celerioService.replace(link3, new_dbpwd, old_dbpassword);
			//userService.replace(link3, newgenJava, oldgenJava);
			celerioService.replace(link3, new_celerio_pack,old_celerio_pack);
			celerioService.replace(link3, new_pack_backend,old_pack_backend);
			celerioService.replace(link3, new_version,old_version);

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

			celerioService.replace(link3, newjdbcType, oldjdbcType);
			celerioService.replace(link3, newjdbcgid, oldjdbcgid);
			celerioService.replace(link3, newjdbcartid, oldjdbcartid);
			celerioService.replace(link3, newjdbcDriver, oldjdbcDriver);
			celerioService.replace(link3, new_jdbc_url, old_jdbc_url);
			celerioService.replace(link3, new_jdbc_user, old_jdbc_user);
			celerioService.replace(link3, new_jdbc_passwd, old_jdbc_password);
			celerioService.replace(link3, newhibernateD, oldhibernateD);
			celerioService.replace(link3, newversion, oldversion);
			

			String link4 = "C:\\AutoCodeGeneration\\ivu.apie.database\\ivu.apie.database\\" + app_name + "\\src\\main\\config\\celerio-maven-plugin\\celerio-maven-plugin.xml";
			String newpattern = "";
			for (String i : celerioTableList) {
				newpattern += "<table include=\"true\" pattern=\"" + i + "\"/>\n\t";
			}
			
			String oldpattern = "<table include=\"true\".*";
			celerioService.replace(link4, newpattern, oldpattern);

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

				

				for (String file : celerioFileList) {

					
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
		
		
		
		@PostMapping("/downloadCelerioCode/{zip_name}")
		public String downloadCelerioCode(HttpServletRequest request,HttpServletResponse response, @PathVariable("zip_name") String fileName) throws IOException
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
			return "codeGenerated";
		}

	
/* ***************Picocog project**************************** */	
	
	@PostMapping("/showKeyspaces")
	public String showKeyspaces(Model model,HttpServletRequest request,@ModelAttribute RemoteDb remoteDb) {
		// List<Row> keyspaces = userService.showKeyspaces();
		//String ip = "10.16.70.123";
		String hostip = remoteDb.getHostip();
		
		UUID id = UUID.randomUUID();
		System.out.println(hostip);
		//remoteDb.setHostip(hostIp);
		Random r = new Random();
	//	int appid = r.nextInt(500);
	//	int orgid = r.nextInt(1000);
		remoteDb.setId(id);
		
		
		remoteDb.setHostip(hostip);
		
		if(picocogService.checkIfExists(hostip) == false)
			picocogService.saveUser(remoteDb);
		
		List<JSONObject> js = new ArrayList<JSONObject>();
		js = picocogService.showKeyspaces(hostip);
		model.addAttribute("keyspaces", js);
		model.addAttribute("ip", hostip);
		return "picocogIndex";
	}
	
	@PostMapping("/viewTables/{ip}/{i.get('keyspace_name')}")
	public String viewTables(Model model,@PathVariable("ip") String hostIp ,@PathVariable("i.get('keyspace_name')") String keyspace_name,HttpServletRequest request)
			throws ClassNotFoundException, SQLException {
		List<JSONObject> js = new ArrayList<JSONObject>();
//		String ip = "10.16.70.123";
		js = picocogService.showKeyspaces(hostIp);
		model.addAttribute("keyspaces", js);
		request.getSession().setAttribute("keyspaceName", keyspace_name);
		request.getSession().setAttribute("keyspaceList", js);
		List<String> tableList = picocogService.getTableList(hostIp,keyspace_name);
		String[] array = new String[tableList.size()];
		array = tableList.toArray(array);
		List<JSONObject>[] arrayTableList = new ArrayList[tableList.size()];
		
		for (int arrayIterator = 0, it = 0; arrayIterator < arrayTableList.length; arrayIterator++, it++) {
			arrayTableList[arrayIterator] = picocogService.getColumnDefinition(hostIp,keyspace_name,array[it]);

		}

//		List<JSONObject> jsonObject = new ArrayList<JSONObject>();
//		jsonObject = userService.getColumnDefinition();
		model.addAttribute("colInfo", arrayTableList);

		model.addAttribute("keyspaceName", keyspace_name);
		model.addAttribute("tableList", tableList);
		model.addAttribute("hostIp", hostIp);
		
		request.getSession().setAttribute("arrayTableList", arrayTableList);
		request.getSession().setAttribute("tableList", tableList);
		request.getSession().setAttribute("tableListSize", tableList.size());
		
		return "viewPicocogTableInfo";
	}
	
	String generatePicocogZipEntry(String file,String link){
    	return file.substring(link.length()+1, file.length());
    }

	 public void generatePicocogFileList(File node,String link)
	 {

	    	//add file only
			if(node.isFile()){
				picocogFileList.add(generatePicocogZipEntry(node.getAbsoluteFile().toString(),link));
			}
				
			if(node.isDirectory()){
				String[] subNote = node.list();
				for(String filename : subNote){
					generatePicocogFileList(new File(node, filename),link);
				}
			}
	 
	  }
	
	@PostMapping("/genPojo/{hostIp}/{keyspaceName}")
	public String genPojo(Model model,HttpServletRequest request,@PathVariable("hostIp") String hostIp,@ModelAttribute HostUser hostUser,@PathVariable("keyspaceName") String keyspaceName,CodeGenDetails cgdetails) throws ClassNotFoundException, SQLException, IOException {
		String checked = "";
		picocogTableList.clear();
		String getChecked[] = request.getParameterValues("check");
		List<JSONObject> js = new ArrayList<JSONObject>();
		int flag=1;
		String packageName = cgdetails.getPackagename();
		String projectName = cgdetails.getProjectname();
		
		// Change this link to suit your system
		File newDirectory = new File("C:\\Users\\abhishek.i.b\\Desktop\\abhi\\cassandra\\" + projectName);
		newDirectory.mkdir();
		
		for(int tableIterator = 0; tableIterator < getChecked.length; tableIterator++)
		{
			checked = getChecked[tableIterator];
			//String keyspaceName = (String) request.getSession().getAttribute("keyspaceName");
			js = picocogService.getColumnDefinition(hostIp,keyspaceName,checked);
			picocogService.picoWriterMethod(js,checked,packageName,projectName);
			picocogService.repositoryMethod(js,checked,packageName,projectName);
			picocogService.createServiceClass(js,checked,packageName,projectName);
			
			picocogTableList.add(checked);
			
			// Change this link to suit your system
			File f = new File("C:\\Users\\abhishek.i.b\\Desktop\\abhi\\cassandra\\" + projectName);
			if(f.exists() == false)
			{
				flag=0;
			}
			else
				continue;
		}
		
		
		  List<JSONObject> keyspaceList = new ArrayList<JSONObject>(); 
		  keyspaceList = (List<JSONObject>) request.getSession().getAttribute("keyspaceList");
		  int tableListSize = (int) request.getSession().getAttribute("tableListSize");
		  List<JSONObject>[] arrayTableList = new ArrayList[tableListSize];
		  arrayTableList = (List<JSONObject>[]) request.getSession().getAttribute("arrayTableList");
		  
		  DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  Date date = new Date();
		  String finalDate = df1.format(date);
		  
		  int userid = picocogService.findGreatestId(hostIp);
		  userid++;
		  
		  model.addAttribute("keyspaces", keyspaceList);
		  model.addAttribute("colInfo", arrayTableList);
		  int appid = 0,orgid = 0;UUID uuid = null;
		  List<JSONObject> ids = picocogService.findids(hostIp);
		  for(JSONObject jsonObject:ids)
		  {
			  appid = jsonObject.getInt("appid");
			  orgid = jsonObject.getInt("orgid");
			  uuid = (UUID) jsonObject.get("id");
		  }
		  
		  String gentables = "";
			for (String name : picocogTableList) {
				gentables = gentables + name + ",";
			}
		  
		  cgdetails.setUserid(userid);
		  cgdetails.setAppid(appid);
		  cgdetails.setOrgid(orgid);
		  cgdetails.setHostip(hostIp);
		  cgdetails.setKeyspacename(keyspaceName);
		  cgdetails.setUuid(uuid);
		  cgdetails.setActive(true);
		  cgdetails.setGendate(finalDate);
//		  String packagename = cgdetails.getPackagename();
//		  String projectname = cgdetails.getProjectname();
		  cgdetails.setPackagename(packageName);
		  cgdetails.setProjectname(projectName);
		  cgdetails.setGentables(gentables);
		  
		  picocogService.savedbdetails(cgdetails);
		  
	/* *************zipping******************** */	  
		  
			
		
		if(newDirectory.exists())
		{
			// Change this link to suit your system
			File fileToZip=new File("C:\\Users\\abhishek.i.b\\Desktop\\abhi\\cassandra\\" + projectName);
			String link="C:\\Users\\abhishek.i.b\\Desktop\\abhi\\cassandra\\" + projectName;
			generatePicocogFileList(fileToZip,link);
			
			String OUTPUT_ZIP_FILE = "C:\\Users\\abhishek.i.b\\Desktop\\abhi\\cassandra\\"+projectName+".zip";
			String SOURCE_FOLDER = "C:\\Users\\abhishek.i.b\\Desktop\\abhi\\cassandra\\"+projectName;
			String zip_name = projectName+".zip";

			byte[] buffer = new byte[1024];
			

			try {

				FileOutputStream fos = new FileOutputStream(OUTPUT_ZIP_FILE);
				ZipOutputStream zos = new ZipOutputStream(fos);

				

				for (String file : picocogFileList) {

					
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
			model.addAttribute("zip_name", zip_name);
			
		}
			
			
		  
	/* *************************************************** */
		
		if(flag==1)
			model.addAttribute("outputMsg", "Code generated");
		else if(flag==0)
			model.addAttribute("outputMsg", "Error while trying to generate code");
		
//		model.addAttribute("done","Pojo's generated");
		
		return "viewPicocogTableInfo";
	}
	
	@PostMapping("/downloadPicocogCode/{zip_name}")
	public String downloadPicocogCode(HttpServletRequest request,HttpServletResponse response, @PathVariable("zip_name") String fileName,Model model) throws IOException
	{
		 
		
		
		// Change this link to suit your system
		String DIRECTORY = "C:\\Users\\abhishek.i.b\\Desktop\\abhi\\cassandra";
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
	     
		model.addAttribute("zipped", "Folder zipped");
		return "viewPicocogTableInfo";
	}
	 
	@PostMapping("/jumpToId")
	public String jumpToId(Model model,@ModelAttribute HostUser hostUser, HttpServletRequest request )
	{
		
		String keyspaceName = (String) request.getSession().getAttribute("keyspaceName");
		String pageId = hostUser.getPageId();
		return "viewPicocogTableInfo";
	}
	

}
