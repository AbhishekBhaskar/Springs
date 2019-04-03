package com.CodeGenAPI.Services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.CodeGenAPI.Repository.PicocogDBRepo;
import com.CodeGenAPI.Repository.PicocogRepository;
import com.CodeGenAPI.Modal.CodeGenDetails;
import com.CodeGenAPI.Modal.RemoteDb;
import com.CodeGenAPI.Modal.User;

import com.CodeGenAPI.picocog.*;

@Service
@Transactional
public class PicocogService {
	private final PicocogRepository picocogRepository;
	private final PicocogDBRepo dbRepository;

	public PicocogService(PicocogRepository picocogRepository,PicocogDBRepo dbRepository) {
		this.picocogRepository = picocogRepository;
		this.dbRepository = dbRepository;
	}

	/*
	 * public void saveUser(UUID id,boolean active,int appid,String hostip,int
	 * orgid) { userRepository.saveUser(id, active, appid, hostip, orgid); }
	 */
	
	public void saveUser(RemoteDb remoteDb)
	{
		picocogRepository.save(remoteDb);
	}
	
	public void savedbdetails(CodeGenDetails cgdetails)
	{
		dbRepository.save(cgdetails);
	}
	
	
	public List<JSONObject> showKeyspaces(String hostIp)
	{
		JSONObject js = new JSONObject();
		List<JSONObject> jsonArr = new ArrayList<JSONObject>();
		Cluster cluster = null;
		
		try
		{
			cluster = Cluster.builder().addContactPoints(hostIp).withoutMetrics().build();
			Session session = cluster.connect();
			String query = "Select * from system_schema.keyspaces";
			ResultSet result = session.execute(query);
			
			for(Row row:result)
			{				
				js = new JSONObject();
				for (int i = 0; i < 3; i++)
				{
					switch(i)
					{
					case 0: js.put("keyspace_name", row.getString(i));
						break;
					/*
					 * case 1: js.put("durable_writes", row.getString(i)); break;
					 */
					/*
					 * case 2: js.put("replication", row.getString(i)); break;
					 */
					}
				}
				jsonArr.add(js);
			}
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return jsonArr;
	}


	String type = "";
	
	
	public List<JSONObject> getColumnDefinition(String hostIp,String keyspace,String table_name) throws ClassNotFoundException, SQLException {
		JSONObject js = new JSONObject();
		List<JSONObject> jsonArr = new ArrayList<JSONObject>();
		Cluster cluster = null;

		try {
			cluster = Cluster.builder().addContactPoint(hostIp).withoutMetrics().build();
			Session session = cluster.connect();
			String query = "SELECT table_name,column_name,clustering_order,kind,type from system_schema.columns where  keyspace_name = '" + keyspace + "' and table_name = '" + table_name + "'";
			ResultSet result = session.execute(query);
			if (result != null)
				System.out.println("Query executing");

			for (Row row : result) {
				js = new JSONObject();
				for (int i = 0; i < 5; i++) {
					System.out.println("index :: " + i + " value :: " + row.getString(i)); //
					//js.put("Table_Name", table_name);
					switch (i) {
					case 0:
						js.put("Table_Name", row.getString(i));
						break;
					case 1: 
						js.put("Column_Name", row.getString(i));
						break;
					case 2:
						js.put("Clustering_Order", row.getString(i));
						break;
					case 3:						
						js.put("Kind", row.getString(i));
						break;
					case 4:						
						js.put("Type", row.getString(i));
						type = row.getString(i);
						break;
					}

				}
				System.out.println();
				jsonArr.add(js);
			}

			/*
			 * for(Row row:result) { for (ColumnDefinitions.Definition definition :
			 * row.getColumnDefinitions()) { System.out.printf("Column %s has type %s%n",
			 * definition.getName(),definition.getType()); } System.out.println(); }
			 */

		} catch (Exception e) {
			System.out.println(e);
		}
		
		return jsonArr;
	}
	
	public List<String> getTableList(String hostIp,String keyspace)
	{
		Cluster cluster = null;
		List<String> tableNames = new ArrayList<String>();
		JSONObject js = new JSONObject();
		List<JSONObject> jsonArr = new ArrayList<JSONObject>();
		try {
			cluster = Cluster.builder().addContactPoint(hostIp).withoutMetrics().build();
			Session session = cluster.connect();
			String query = "SELECT table_name from system_schema.tables where keyspace_name = '" + keyspace + "'";
			ResultSet result = session.execute(query);
			for(Row row:result)
			{
				String value = row.getString("table_name");
				tableNames.add(value);
			}
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return tableNames;
	}
	
	public String toCamelCase(String s)
	{
		/*
		 * for(int i=0;i<s.length();i++) { if(i==0) { s = s.replace(s.charAt(0),
		 * Character.toUpperCase(s.charAt(0))); } }
		 */
		char firstChar = s.charAt(0);
		char fc = Character.toUpperCase(firstChar);
		String str = fc + "";
		for(int i=1;i<s.length();i++)
		{
			str += s.charAt(i);
		}
		
		str = str.replaceAll("_", "");
		//s = s.replace(s.charAt(0), Character.toUpperCase(s.charAt(0)));
		return str;
	}
	
	public void picoWriterMethod(List<JSONObject> jsParam,String table_name,String packageNameParam,String projectName) throws IOException
	{
		File dir = new File("C:\\Users\\abhishek.i.b\\Desktop\\abhi\\cassandra\\" + projectName + "\\domain");
		dir.mkdir();
		
		
		
		FileWriter writer = null;
		try
		{
			String tableName = table_name;
			String className = toCamelCase(tableName);
			
			// Change this link to suit your system
			File f = new File("C:\\Users\\abhishek.i.b\\Desktop\\abhi\\cassandra\\" + projectName + "\\domain\\"+ className + ".java");
			f.createNewFile();
		
			PicoWriter outer = new PicoWriter();
			String packageName = packageNameParam;			
			
			outer.writeln("import javax.persistence.Column;" + 
					"\r" + 
					"import javax.persistence.Entity;" + 
					"\r" + 
					"import javax.persistence.GeneratedValue;" + 
					"\r" + 
					"import javax.persistence.GenerationType;" + 
					"\r" + 
					"import javax.persistence.Id;" + 
					"\r" + 
					"import javax.persistence.Table;" + 
					"\r" + 
					"import javax.persistence.Transient;" + 
					"\r" + 
					"import javax.validation.constraints.NotNull;");
			
			outer.writeln("import lombok.Getter;" + 
					"\r" + 
					"import lombok.Setter;");
			
			outer.writeln("package "+packageName+";");
			outer.writeln("");
			outer.writeln("@Entity");
			outer.writeln("@Table(\"" + tableName + "\")");
			outer.writeln("@Getter");
			outer.writeln("@Setter");
			
			outer.writeln_r("public class " + className + "{");
			PicoWriter inner = outer.createDeferredWriter();
			outer.writeln("");
			
			for(JSONObject iterator:jsParam)
			{
				JSONObject js = new JSONObject();
				js = iterator;
				String column_name = (String) js.get("Column_Name");
				String setcc_column_name = "set" + toCamelCase(column_name);
				String getcc_column_name = "get" + toCamelCase(column_name);
				String typeField = (String) js.get("Type");
				String kind = (String) js.get("Kind");
				
					switch(typeField)
					{
					case "text": if(kind.equals("partition_key"))
								 {
									 inner.writeln("@PrimaryKey");	
								 }
								 inner.writeln("String " + column_name + ";");
								 break;
					
					case "bigint":
					case "int": if(kind.equals("partition_key"))
								 {
									 inner.writeln("@PrimaryKey");	
								 }
								 inner.writeln("int " + column_name + ";");
								 break;
					
					case "timestamp": if(kind.equals("partition_key"))
								 {
									 inner.writeln("@PrimaryKey");	
								 }
								 inner.writeln("String " + column_name + ";");
								 break;
								 
					default: if(kind.equals("partition_key"))
							 {
								 inner.writeln("@PrimaryKey");	
							 }
							 inner.writeln(typeField + " " + column_name + ";");
								 
					}
			
			
					
			}
			outer.writeln("");
			outer.writeln_l("}");
			//System.out.println(outer.toString());
			writer = new FileWriter(f);
			writer.write(outer.toString());
			writer.flush();
			writer.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
}
	
	public void repositoryMethod(List<JSONObject> jsParam,String table_name,String packageNameParam,String projectName) throws IOException
	{
		// Change this link to suit your system
		File dir = new File("C:\\Users\\abhishek.i.b\\Desktop\\abhi\\cassandra\\" + projectName + "\\repository");
		dir.mkdir();
		
		
		try
		{
			PicoWriter outer = new PicoWriter();
			String packageName = packageNameParam;			
			
			String tableName = table_name;
			String className = toCamelCase(tableName);
			
			// Change this link to suit your system
			File f = new File("C:\\Users\\abhishek.i.b\\Desktop\\abhi\\cassandra\\" + projectName + "\\repository\\"+ className + "Repository.java");
			f.createNewFile();
			FileWriter writer = null;
			
			outer.writeln("package "+packageName+";");
			outer.writeln("");
			outer.writeln("import org.springframework.data.jpa.repository.*;\n" + 
					"import org.springframework.data.repository.CrudRepository;\n" + 
					"import org.springframework.stereotype.Repository;\n");
			outer.writeln("");
			outer.writeln("import " + packageName + ".domain.*;");
			outer.writeln("");
			outer.writeln("@Repository");
			String dataType = "";
			for(JSONObject iterator:jsParam)
			{
				JSONObject js = new JSONObject();
				js = iterator;
				String kind = (String) js.get("Kind");
				String typeField = (String) js.get("Type");
				
				if(kind.equals("partition_key"))
				{
					dataType = typeField;
				}
			}
			
			switch(dataType)
			{
			case "bigint":
			case "int":  outer.writeln_r("public interface " + className + "Repository extends CrudRepository<" + className + ", " + "int" + "> {");
						break;
						
			case "text": outer.writeln_r("public interface " + className + "Repository extends CrudRepository<" + className + ", " + "String" + "> {");
						break;
						
			default: outer.writeln_r("public interface " + className + "Repository extends CrudRepository<" + className + ", " + dataType + "> {");
					break;
			}
			
			//outer.writeln_r("public interface " + className + "Repository extends CrudRepository<" + className + ", " + dataType + "> {");
			outer.writeln("");
			outer.writeln_l("}");
			writer = new FileWriter(f);
			writer.write(outer.toString());
			writer.flush();
			writer.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public void createServiceClass(List<JSONObject> jsParam,String table_name,String packageNameParam,String projectName) throws IOException
	{
		// Change this link to suit your system
		File dir = new File("C:\\Users\\abhishek.i.b\\Desktop\\abhi\\cassandra\\" + projectName + "\\services");
		dir.mkdir();
		
		
		try
		{
			PicoWriter outer = new PicoWriter();
			String packageName = packageNameParam;			
			
			String tableName = table_name;
			String className = toCamelCase(tableName);
			
			// Change this link to suit your system
			File f = new File("C:\\Users\\abhishek.i.b\\Desktop\\abhi\\cassandra\\" + projectName + "\\services\\"+ className + "Service.java");
			f.createNewFile();
			FileWriter writer = null;
			
			outer.writeln("package "+packageName+";");
			outer.writeln("import java.util.ArrayList;\r\n" + 
					"import java.util.List;\r\n" + 
					"import java.util.stream.Collectors;\r\n" + 
					"\r\n" + 
					"import javax.inject.Inject;\r\n" + 
					"\r\n" + 
					"import org.springframework.data.domain.Example;\r\n" + 
					"import org.springframework.data.domain.ExampleMatcher;\r\n" + 
					"import org.springframework.data.domain.Page;\r\n" + 
					"import org.springframework.stereotype.Service;\r\n" + 
					"import org.springframework.transaction.annotation.Transactional;");
			outer.writeln("");
			outer.writeln("import " + packageName + ".domain.*;");
			outer.writeln("import " + packageName + ".repository.*;");
			outer.writeln("");
			outer.writeln("@Service");
			outer.writeln_r("public class " + className + "Service {");
			
			String repoClass = className + "Repository";
			String repoObject = repoClass.replace(repoClass.charAt(0), Character.toLowerCase(repoClass.charAt(0)));
			
			outer.writeln("@Inject");
			outer.writeln("private " + className + "Repository " + repoObject + ";");
			outer.writeln("");
			
			String dataType = "";
			String colName = "";
			for(JSONObject iterator:jsParam)
			{
				JSONObject js = new JSONObject();
				js = iterator;
				String kind = (String) js.get("Kind");
				String typeField = (String) js.get("Type");
				String column_name = (String) js.get("Column_Name"); 
				if(kind.equals("partition_key"))
				{
					dataType = typeField;
					colName = column_name;
				}
			}
			
			outer.writeln("@Transactional(readOnly = true)");
			outer.writeln("public " + className + " findOne(" + dataType + " " + colName + ") {");
			//outer.writeln("toDTO(" + className+ "Repository.findById(" + colName + ").get()" + ");");
			outer.writeln("return " + className+ "Repository.findById(" + colName + ").get();");
			outer.writeln("}");
			outer.writeln("");
			
			outer.writeln("@Transactional(readOnly = true)");
			outer.writeln("public List<" + className + "> findAll() {");
			outer.writeln("Iterable<" + className + "> page = " + className + "Repository.findAll();");
			outer.writeln("List<" + className + "> content = new ArrayList<" + className + ">();");
			outer.writeln("page.forEach(content::add);");
			outer.writeln("");
			outer.writeln("return content;");
			outer.writeln("}");
			outer.writeln("");
			
			String classObject = className.replace(repoClass.charAt(0), Character.toLowerCase(repoClass.charAt(0)));
			outer.writeln("public " + className + "DTO toDTO(" + className + " " + classObject + ") {");
			outer.writeln("return toDTO(" + classObject + ", 1);");
			outer.writeln("}");
			outer.writeln("");
			
			outer.writeln("@Transactional");
			outer.writeln("public void save(" + className + " " + classObject + ") {");
			outer.writeln(className + "Repository.save(" + classObject + ");");
			outer.writeln("}");
			outer.writeln("");
			outer.writeln_l("}");
			
			writer = new FileWriter(f);
			writer.write(outer.toString());
			writer.flush();
			writer.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public boolean checkIfExists(String hostip)
	{
		/*
		 * String ip = userRepository.checkIfExists(hostip); return ip;
		 */
		int f=1;
		
		for(RemoteDb user:picocogRepository.findAll())
		{
			if(user.getHostip().equals(hostip))
				f=0;
		}
		if(f==0)
			return true;
		else
			return false;
		
	}
	
	public String findByHostip(String hostip)
	{
		ResultSet resultRow = picocogRepository.findByHostip(hostip);
		
		String ip = null;
		for (Row row : resultRow)
		{
			
			for (int i = 0; i < 5; i++)
			{
				if(i==3)
				{
					ip = row.getString(i);
				}
				else continue;
//				switch(i)
//				{
//					case 3: ip = row.getString(i);
//							System.out.println(ip);
//							break;
//					default:System.out.println("error in getting hostip");
//				}
					
			}
		}
		return ip;
	}
	
	public List<JSONObject> findids(String hostip)
	{
		List<JSONObject> jsonlist = new ArrayList<JSONObject>();
		JSONObject js = new JSONObject();
		String ip = findByHostip(hostip);
		for(RemoteDb remotedb:picocogRepository.findAll())
		{
			//js = new JSONObject();
			if(remotedb.getHostip().equals(ip))
			{
				
				js.put("appid", remotedb.getAppid());
				js.put("orgid", remotedb.getOrgid());
				js.put("id", remotedb.getId());
			}
			jsonlist.add(js);
		}
		System.out.println(jsonlist);
		return jsonlist;
	}
	
	public int findGreatestId(String hostip)
	{
		int maxId=0;
		for(CodeGenDetails cgdetails:dbRepository.findAll())
		{
			if(cgdetails.getUserid()>maxId)
				maxId = cgdetails.getUserid();
		}
		return maxId;
	}
	
}