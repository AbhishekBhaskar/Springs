package com.empApp.Controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import com.empApp.model.CodeGenDetails;
import com.empApp.model.HostUser;
import com.empApp.model.RemoteDb;
import com.empApp.model.User;
import com.empApp.Services.UserService;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.empApp.Repository.UserRepository;

@Controller
public class UserController {
	@Autowired
	private UserService userService;
	List<String> fileList = new ArrayList<String>();
	List<String> l2 = new ArrayList<String>();
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request) {
		fileList.clear();
		return "index";
	}

	@PostMapping("/showKeyspaces")
	public String showKeyspaces(Model model,HttpServletRequest request,@ModelAttribute RemoteDb remoteDb) {
		// List<Row> keyspaces = userService.showKeyspaces();
		//String ip = "10.16.70.123";
		String hostip = remoteDb.getHostip();
		UUID id = UUID.randomUUID();
		
		//remoteDb.setHostip(hostIp);
		Random r = new Random();
	//	int appid = r.nextInt(500);
	//	int orgid = r.nextInt(1000);
		remoteDb.setId(id);
		
		
		remoteDb.setHostip(hostip);
		
		if(userService.checkIfExists(hostip) == false)
			userService.saveUser(remoteDb);
		
		List<JSONObject> js = new ArrayList<JSONObject>();
		js = userService.showKeyspaces(hostip);
		model.addAttribute("keyspaces", js);
		model.addAttribute("ip", hostip);
		return "index";
	}

	

	

	/*
	 * @PostMapping("/showColDef") public String showColDef(Model model) throws
	 * ClassNotFoundException, SQLException {
	 * 
	 * List<JSONObject> js = new ArrayList<JSONObject>(); js =
	 * userService.getColumnDefinition(); model.addAttribute("colInfo", js); return
	 * "index"; }
	 */



	@PostMapping("/viewTables/{ip}/{i.get('keyspace_name')}")
	public String viewTables(Model model,@PathVariable("ip") String hostIp ,@PathVariable("i.get('keyspace_name')") String keyspace_name,HttpServletRequest request)
			throws ClassNotFoundException, SQLException {
		List<JSONObject> js = new ArrayList<JSONObject>();
//		String ip = "10.16.70.123";
		js = userService.showKeyspaces(hostIp);
		model.addAttribute("keyspaces", js);
		request.getSession().setAttribute("keyspaceName", keyspace_name);
		request.getSession().setAttribute("keyspaceList", js);
		List<String> tableList = userService.getTableList(hostIp,keyspace_name);
		String[] array = new String[tableList.size()];
		array = tableList.toArray(array);
		List<JSONObject>[] arrayTableList = new ArrayList[tableList.size()];
		
		for (int arrayIterator = 0, it = 0; arrayIterator < arrayTableList.length; arrayIterator++, it++) {
			arrayTableList[arrayIterator] = userService.getColumnDefinition(hostIp,keyspace_name,array[it]);

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
		
		return "viewTableInfo";
	}
	
	String generateZipEntry(String file,String link){
    	return file.substring(link.length()+1, file.length());
    }

	 public void generateFileList(File node,String link)
	 {

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
	
	@PostMapping("/genPojo/{hostIp}/{keyspaceName}")
	public String genPojo(Model model,HttpServletRequest request,@PathVariable("hostIp") String hostIp,@ModelAttribute HostUser hostUser,@PathVariable("keyspaceName") String keyspaceName,CodeGenDetails cgdetails) throws ClassNotFoundException, SQLException, IOException {
		String checked = "";
		l2.clear();
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
			js = userService.getColumnDefinition(hostIp,keyspaceName,checked);
			userService.picoWriterMethod(js,checked,packageName,projectName);
			userService.repositoryMethod(js,checked,packageName,projectName);
			userService.createServiceClass(js,checked,packageName,projectName);
			
			l2.add(checked);
			
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
		  
		  int userid = userService.findGreatestId(hostIp);
		  userid++;
		  
		  model.addAttribute("keyspaces", keyspaceList);
		  model.addAttribute("colInfo", arrayTableList);
		  int appid = 0,orgid = 0;UUID uuid = null;
		  List<JSONObject> ids = userService.findids(hostIp);
		  for(JSONObject jsonObject:ids)
		  {
			  appid = jsonObject.getInt("appid");
			  orgid = jsonObject.getInt("orgid");
			  uuid = (UUID) jsonObject.get("id");
		  }
		  
		  String gentables = "";
			for (String name : l2) {
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
		  
		  userService.savedbdetails(cgdetails);
		  
	/* *************zipping******************** */	  
		  
			
		
		if(newDirectory.exists())
		{
			// Change this link to suit your system
			File fileToZip=new File("C:\\Users\\abhishek.i.b\\Desktop\\abhi\\cassandra\\" + projectName);
			String link="C:\\Users\\abhishek.i.b\\Desktop\\abhi\\cassandra\\" + projectName;
			generateFileList(fileToZip,link);
			
			String OUTPUT_ZIP_FILE = "C:\\Users\\abhishek.i.b\\Desktop\\abhi\\cassandra\\"+projectName+".zip";
			String SOURCE_FOLDER = "C:\\Users\\abhishek.i.b\\Desktop\\abhi\\cassandra\\"+projectName;
			String zip_name = projectName+".zip";

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
			model.addAttribute("zip_name", zip_name);
			
		}
			
			
		  
	/* *************************************************** */
		
		if(flag==1)
			model.addAttribute("outputMsg", "Code generated");
		else if(flag==0)
			model.addAttribute("outputMsg", "Error while trying to generate code");
		
//		model.addAttribute("done","Pojo's generated");
		
		return "viewTableInfo";
	}
	
	@PostMapping("/downloadCode/{zip_name}")
	public String downloadCode(HttpServletRequest request,HttpServletResponse response, @PathVariable("zip_name") String fileName,Model model) throws IOException
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
		return "viewTableInfo";
	}
	 
	@PostMapping("/jumpToId")
	public String jumpToId(Model model,@ModelAttribute HostUser hostUser, HttpServletRequest request )
	{
		
		String keyspaceName = (String) request.getSession().getAttribute("keyspaceName");
		String pageId = hostUser.getPageId();
		return "viewTableInfo";
	}
	
	 

}