package com.empApp.Services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
import com.empApp.Repository.UserRepository;
import com.empApp.model.User;

import com.empApp.picocog.*;

@Service
@Transactional
public class UserService {
	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void saveUser(User user) {
		userRepository.save(user);
	}

	public List<User> showUsers() {
		List<User> userList = new ArrayList<User>();
		for (User user : userRepository.findAll()) {
			userList.add(user);
		}
		return userList;
	}

	/*
	 * public Connection connectDatabase() throws ClassNotFoundException,
	 * SQLException { Connection connection=null;
	 * Class.forName("org.apache.cassandra.cql.jdbc.CassandraDriver"); connection =
	 * DriverManager.getConnection("jdbc:cassandra://localhost:9042/people"); return
	 * connection; }
	 */

	String type = "";
	
	
	public List<JSONObject> getColumnDefinition() throws ClassNotFoundException, SQLException {
		JSONObject js = new JSONObject();
		List<JSONObject> jsonArr = new ArrayList<JSONObject>();
		Cluster cluster = null;

		try {
			cluster = Cluster.builder().addContactPoint("localhost").withoutMetrics().build();
			Session session = cluster.connect();
			String query = "SELECT column_name,clustering_order,kind,type from system_schema.columns where  keyspace_name = 'people' and table_name = 'user'";
			ResultSet result = session.execute(query);
			if (result != null)
				System.out.println("Query executing");

			for (Row row : result) {
				js = new JSONObject();
				for (int i = 0; i < 4; i++) {
					System.out.println("index :: " + i + " value :: " + row.getString(i)); //
					//js.put("Table_Name", table_name);
					switch (i) {
					case 0: 
						js.put("Column_Name", row.getString(i));
						break;
					case 1:
						js.put("Clustering_Order", row.getString(i));
						break;
					case 2:						
						js.put("Kind", row.getString(i));
						break;
					case 3:						
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
	
	public String toCamelCase(String s)
	{
		s = s.replace(s.charAt(0), Character.toUpperCase(s.charAt(0)));
		return s;
	}
	
	public void picoWriterMethod(List<JSONObject> jsParam)
	{
		PicoWriter outer = new PicoWriter();
		String packageName = "packageName";
		String className = "className";
		String tableName = "tableName";
		outer.writeln("import javax.persistence.Column;\r\n" + 
				"\r\n" + 
				"import javax.persistence.Entity;\r\n" + 
				"\r\n" + 
				"import javax.persistence.GeneratedValue;\r\n" + 
				"\r\n" + 
				"import javax.persistence.GenerationType;\r\n" + 
				"\r\n" + 
				"import javax.persistence.Id;\r\n" + 
				"\r\n" + 
				"import javax.persistence.Table;\r\n" + 
				"\r\n" + 
				"import javax.persistence.Transient;\r\n" + 
				"\r\n" + 
				"import javax.validation.constraints.NotNull;");
		
		outer.writeln("import lombok.Getter;\r\n" + 
				"\r\n" + 
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
		//JSONObject js = new JSONObject();
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
//						 outer.writeln_r("public void " + setcc_column_name + "(String " + column_name +") {");
//						 outer.writeln("this." + column_name + " = " + column_name + ";");
//						 outer.writeln_l("}");
//						 outer.writeln_r("public void " + getcc_column_name + "() {");
//						 outer.writeln("return " + column_name);
//						 outer.writeln_l("}");
						 break;
			
			case "int": if(kind.equals("partition_key"))
						 {
							 inner.writeln("@PrimaryKey");	
						 }
						 inner.writeln("int " + column_name + ";");
//						 outer.writeln_r("public void " + setcc_column_name + "(int " + column_name +") {");
//						 outer.writeln("this." + column_name + " = " + column_name + ";");
//						 outer.writeln_l("}");
//						 outer.writeln_r("public void " + getcc_column_name + "() {");
//						 outer.writeln("return " + column_name);
//						 outer.writeln_l("}");
						 break;
						 
			default: throw new RuntimeException("Unknown field type: " + typeField);
			}
				
		}
		outer.writeln("");
		outer.writeln("}");
		System.out.println(outer.toString());
	}
}
