package com.CodeGenAPI.Services;

import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.CodeGenAPI.Modal.Database;
import com.CodeGenAPI.Modal.User;
import com.CodeGenAPI.Modal.organization;
import com.CodeGenAPI.Repository.DBRepository;
import com.CodeGenAPI.Repository.UserRepository;
import com.CodeGenAPI.Repository.org_repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Table;

@Service
@Transactional
public class CelerioService {
	private final UserRepository userRepository;
	private final org_repository orgRepo;
	private final DBRepository dbRepo;

	public CelerioService(UserRepository userRepository, org_repository orgRepo, DBRepository dbRepo) {
		this.userRepository = userRepository;
		this.orgRepo = orgRepo;
		this.dbRepo = dbRepo;
	}

	public List<String> get_tables() {
		List<String> tables = new ArrayList<String>();
		for (String table : userRepository.get_tables()) {
			tables.add(table);
		}
		return tables;
	}

	public List<String> conn_db(String host, String uname, String passwd) {
		Connection conn = null;
		try {
			String driverName = "com.mysql.jdbc.Driver";
			Class.forName(driverName);

			String serverName = host;

			String schema = "e-comm";

			// String url = "jdbc:mysql://" + serverName + "/" + schema;
			String url = "jdbc:mysql://" + serverName;
			String username = "root";

			// String password = "password";
			String password = "";
			/*
			 * if(serverName.equals("localhost")) password="Sairam@27"; else
			 * if(serverName.equals("10.16.35.127")) password="password"; else
			 * password="Sairam@27";
			 */

			conn = DriverManager.getConnection(url, uname, passwd);
		} catch (ClassNotFoundException e) {

			System.out.println("Could not find the database driver " + e.getMessage());
		} catch (SQLException e) {

			System.out.println("Could not connect to the database " + e.getMessage());
		}
		String tablename = "";

		List<String> l = new ArrayList<String>();
		List<String> l2 = new ArrayList<String>();
		List<String> l3 = new ArrayList<String>();
		try {
			DatabaseMetaData metadata = conn.getMetaData();

			String[] types = { "TABLE" };
			// ResultSet resultSet = metadata.getTables(null, "e-comm", "%", types);
			ResultSet resultSet = metadata.getTables(null, null, "%", types);
			while (resultSet.next()) {

				tablename = resultSet.getString(3);
				l.add(tablename);
				String tableCatalog = resultSet.getString(1);
				l2.add(tableCatalog);

				String tableschema = resultSet.getString(2);

			}

			for (String a : l2) {
				if (!l3.contains(a))
					l3.add(a);
			}

		} catch (SQLException e) {

			System.out.println("Could not get database metadata " + e.getMessage());
		}

		return l3;
	}

	public List<String> show_tables(String schema_name, String host,String uname, String passwd) {
		Connection conn = null;
		try {
			String driverName = "com.mysql.jdbc.Driver";
			Class.forName(driverName);

			String serverName = host;

			String schema = schema_name;

			String url = "jdbc:mysql://" + serverName + "/" + schema;

			String username = uname;

			// String password = "Sairam@27";
			String password = "";
			if (serverName.equals("localhost"))
				password = "Sairam@27";
			else if (serverName.equals("10.16.35.127"))
				password = "password";
			else
				password = "Sairam@27";

			conn = DriverManager.getConnection(url, username, passwd);
		} catch (ClassNotFoundException e) {

			System.out.println("Could not find the database driver " + e.getMessage());
		} catch (SQLException e) {

			System.out.println("Could not connect to the database " + e.getMessage());
		}
		String tablename = "";

		List<String> l = new ArrayList<String>();
		List<String> l2 = new ArrayList<String>();
		List<String> l3 = new ArrayList<String>();
		try {
			DatabaseMetaData metadata = conn.getMetaData();

			String[] types = { "TABLE" };
			ResultSet resultSet = metadata.getTables(schema_name, null, "%", types);

			while (resultSet.next()) {

				tablename = resultSet.getString(3);
				// System.out.println("->" + tablename);
				l.add(tablename);
				String tableCatalog = resultSet.getString(1);
				l2.add(tableCatalog);

				String tableschema = resultSet.getString(2);

			}

		} catch (SQLException e) {

			System.out.println("Could not get database metadata " + e.getMessage());
		}

		return l;
	}
		
	public List<JSONObject> get_desc_values(String table_name, String host, String schema_name, String uname,String passwd) throws SQLException {

		JSONObject js = new JSONObject();
		List<JSONObject> jsonArr = new ArrayList<JSONObject>();
		try {
			Connection conn;
			String driverName = "com.mysql.jdbc.Driver";
			Class.forName(driverName);

			String serverName = host;

			String schema = "userdb";

			String url = "jdbc:mysql://" + serverName;

			String username = "root";

			// String password = "Sairam@27";
			String password = "";
			if (serverName.equals("localhost"))
				password = "Sairam@27";
			else if (serverName.equals("10.16.35.127"))
				password = "password";
			else
				password = "Sairam@27";

			conn = DriverManager.getConnection(url, uname, passwd);

			Statement stmt = conn.createStatement();
			// ResultSet rs = stmt.executeQuery("desc `e-comm`." + table_name);
			ResultSet rs = stmt.executeQuery("desc `" + schema_name + "`." + table_name);
			// int index=1;

			while (rs.next()) {
				// String name = rs.getString(0);
				js = new JSONObject();
				for (int i = 1; i <= 6; i++) {
					System.out.println("index :: " + i + " value :: " + rs.getString(i));
					// js.put("Table_Name", table_name);
					switch (i) {
					case 1: // hmm.put("Column_Name", rs.getString(0));
						js.put("Column_Name", rs.getString(i));
						break;
					case 2: /* hmm.put("Type", rs.getString(1)); */
						js.put("Type", rs.getString(i));
						break;
					case 3:/* hmm.put("Null", rs.getString(2)); */
						js.put("Null", rs.getString(i));
						break;
					case 4: /* hmm.put("Key", rs.getString(3)); */
						js.put("Key", rs.getString(i));
						break;
					case 5: /* hmm.put("Default", rs.getString(4)); */
						js.put("Default", rs.getString(i));
						break;
					case 6:
						js.put("Extra", rs.getString(i));
						break;
					}

				}
				js.put("Table_Name", table_name);
				jsonArr.add(js);
				System.out.println("*****************************************************" + jsonArr);

				// hm.put(table_name, hmm);

			}
		} catch (ClassNotFoundException e) {
			System.out.println("Could not find the database driver " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Could not connect to the database " + e.getMessage());
		}
		return jsonArr;
	}

	public void saveDB(Database db) {
		dbRepo.save(db);
	}

	public String findIp(String host_ip) {
		String ip = dbRepo.findByIp(host_ip);
		return ip;
	}

	public void save_transaction(String host_ip,String schema_name,String gen_tables,String datetime,String download_link,String app_name,String package_name,int appid,int orgid) {
		dbRepo.saveTransaction(host_ip, schema_name, gen_tables, datetime, download_link, app_name, package_name,appid,orgid);
	}
	
	public void savDb(String remotedbname,String hostip,String username,String password,int appid,int orgid)
	{
		dbRepo.saveDb(remotedbname, hostip, username, password, appid, orgid);
	}

	public void replace(String link, String newContent, String oldContent) {
		File f = new File(link);
		StringBuffer old = new StringBuffer();
		// System.out.println("Enter application name");
		//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader reader = null;
		FileWriter writer = null;
		// app_name=br.readLine();

		try {
			reader = new BufferedReader(new FileReader(f));

			// Reading all the lines of input text file into oldContent

			String line = null;

			while ((line = reader.readLine()) != null) {
				old.append(line + System.lineSeparator());

				//line = reader.readLine();
			}
			reader.close();
			// Pattern ptn=Pattern.compile("jdbc:mysql://.*");
			Pattern ptn = Pattern.compile(oldContent);
			Matcher match = ptn.matcher(old);
			String	replaced = match.replaceAll(newContent);
			writer = new FileWriter(f);
			writer.write(replaced);
			writer.flush();
			writer.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		finally {
			try { // Closing the resources

				reader.close();

				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
