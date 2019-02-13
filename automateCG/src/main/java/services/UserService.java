package com.automate.services;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.automate.modal.User;
import com.automate.modal.organization;
import com.automate.repository.UserRepository;
import com.automate.repository.org_repository;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Table;

@Service
@Transactional
public class UserService {
	private final UserRepository userRepository;
	private final org_repository orgRepo;

	public UserService(UserRepository userRepository, org_repository orgRepo) {
		this.userRepository = userRepository;
		this.orgRepo = orgRepo;
	}

	public List<String> get_tables() {
		List<String> tables = new ArrayList<String>();
		for (String table : userRepository.get_tables()) {
			tables.add(table);
		}
		return tables;
	}

	public List<String> conn_db() {
		Connection conn = null;
		try {
			String driverName = "com.mysql.jdbc.Driver";
			Class.forName(driverName);

			String serverName = "localhost";

			String schema = "userdb";

			String url = "jdbc:mysql://" + serverName + "/" + schema;

			String username = "root";

			String password = "Sairam@27";

			conn = DriverManager.getConnection(url, username, password);
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
			ResultSet resultSet = metadata.getTables(null, "e-comm", "%", types);

			while (resultSet.next()) {

				tablename = resultSet.getString(3);
				l.add(tablename);
				String tableCatalog = resultSet.getString(1);
				l2.add(tableCatalog);

				String tableschema = resultSet.getString(2);

			}
			/*
			 * l3.add(l2.get(0)); for(int i=1;i<l2.size();i++) { int c=0; for(int
			 * j=0;j<l3.size();j++) { if(!l2.get(i).equals(l3.get(j))) c++; } if(c>0)
			 * l3.add(l2.get(i)); }
			 */

			for (String a : l2) {
				if (!l3.contains(a))
					l3.add(a);
			}

		} catch (SQLException e) {

			System.out.println("Could not get database metadata " + e.getMessage());
		}

		return l3;
	}

	public List<String> show_tables() {
		Connection conn = null;
		try {
			String driverName = "com.mysql.jdbc.Driver";
			Class.forName(driverName);

			String serverName = "localhost";

			String schema = "userdb";

			String url = "jdbc:mysql://" + serverName + "/" + schema;

			String username = "root";

			String password = "Sairam@27";

			conn = DriverManager.getConnection(url, username, password);
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
			ResultSet resultSet = metadata.getTables(null, "e-comm", "%", types);

			while (resultSet.next()) {

				tablename = resultSet.getString(3);
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

	public ResultSet get_result_set() {
		Connection conn = null;
		try {
			String driverName = "com.mysql.jdbc.Driver";
			Class.forName(driverName);

			String serverName = "localhost";

			String schema = "userdb";

			String url = "jdbc:mysql://" + serverName + "/" + schema;

			String username = "root";

			String password = "Sairam@27";

			conn = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {

			System.out.println("Could not find the database driver " + e.getMessage());
		} catch (SQLException e) {

			System.out.println("Could not connect to the database " + e.getMessage());
		}
		String tablename = "";

		List<String> l2 = new ArrayList<String>();
		List<String> l3 = new ArrayList<String>();
		ResultSet resultset = null;
		try {
			DatabaseMetaData metadata = conn.getMetaData();

			String[] types = { "TABLE" };
			resultset = metadata.getColumns(null, null, "organization", null);

			/*
			 * while (resultSet.next()) {
			 * 
			 * String columnName=resultSet.getString("COLUMN_NAME"); String datatype =
			 * resultSet.getString("DATA_TYPE"); String columnsize =
			 * resultSet.getString("COLUMN_SIZE"); String decimaldigits =
			 * resultSet.getString("DECIMAL_DIGITS"); String isNullable =
			 * resultSet.getString("IS_NULLABLE"); String is_autoIncrment =
			 * resultSet.getString("IS_AUTOINCREMENT"); l.add(columnName);
			 * 
			 * }
			 */

		} catch (SQLException e) {

			System.out.println("Could not get database metadata " + e.getMessage());
		}

		return resultset;
	}

	public List<String> get_column_name() throws SQLException {
		List<String> l = new ArrayList<String>();
		ResultSet resultSet = get_result_set();
		while (resultSet.next()) {
			String columnName = resultSet.getString("COLUMN_NAME");
			l.add(columnName);
		}
		return l;
	}

	public List<String> get_datatype() throws SQLException {
		List<String> l = new ArrayList<String>();
		ResultSet resultSet = get_result_set();
		while (resultSet.next()) {
			String datatype = resultSet.getString("DATA_TYPE");
			l.add(datatype);
		}
		return l;
	}

	public List<String> get_col_size() throws SQLException {
		List<String> l = new ArrayList<String>();
		ResultSet resultSet = get_result_set();
		while (resultSet.next()) {
			String colSize = resultSet.getString("COLUMN_SIZE");
			l.add(colSize);
		}
		return l;
	}

	public List<String> get_nullable() throws SQLException {
		List<String> l = new ArrayList<String>();
		ResultSet resultSet = get_result_set();
		while (resultSet.next()) {
			String is_nullable = resultSet.getString("IS_NULLABLE");
			l.add(is_nullable);
		}
		return l;
	}

	public List<String> get_AI() throws SQLException {
		List<String> l = new ArrayList<String>();
		ResultSet resultSet = get_result_set();
		while (resultSet.next()) {
			String is_AI = resultSet.getString("IS_AUTOINCREMENT");
			l.add(is_AI);
		}
		return l;
	}

	DatabaseMetaData connect_db()
	{
		DatabaseMetaData metadata=null;
		Connection conn = null;
		try {
			String driverName = "com.mysql.jdbc.Driver";
			Class.forName(driverName);

			String serverName = "localhost";

			String schema = "userdb";

			String url = "jdbc:mysql://" + serverName + "/" + schema;

			String username = "root";

			String password = "Sairam@27";

			conn = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {

			System.out.println("Could not find the database driver " + e.getMessage());
		} catch (SQLException e) {

			System.out.println("Could not connect to the database " + e.getMessage());
		}
		
		try {
			metadata = conn.getMetaData();
		}
		catch (SQLException e) {

			System.out.println("Could not get database metadata " + e.getMessage());
		}
		return metadata;
	}
	
	public List<String> get_PK() throws SQLException {

		List<String> l2 = new ArrayList<String>();
		
		ResultSet resultset = null;
		DatabaseMetaData metadata=connect_db();
			String[] types = { "TABLE" };
			resultset = metadata.getPrimaryKeys(null, null, "organization");
			while(resultset.next())
			{
				l2.add(resultset.getString("COLUMN_NAME"));
			}
		return l2;	
	}
	
	public List<String> get_PK_name() throws SQLException {

		List<String> l2 = new ArrayList<String>();
		
		ResultSet resultset = null;
		DatabaseMetaData metadata=connect_db();
			String[] types = { "TABLE" };
			resultset = metadata.getPrimaryKeys(null, null, "organization");
			while(resultset.next())
			{
				l2.add(resultset.getString("PK_NAME"));
			}
		return l2;	
	}
	
	public List<String> get_FK() throws SQLException {

		List<String> l2 = new ArrayList<String>();
		
		ResultSet resultset = null;
		DatabaseMetaData metadata=connect_db();
			String[] types = { "TABLE" };
			resultset = metadata.getImportedKeys(null, null, "organization");
			while(resultset.next())
			{
				l2.add(resultset.getString("FK_NAME"));
			}
		return l2;	
	}

	public List<User> vendor_org_vals() {
		List<User> users = new ArrayList<User>();
		for (User user : userRepository.findAll()) {
			users.add(user);
		}
		return users;
	}

	public List<organization> org_values() {
		List<organization> orgs = new ArrayList<organization>();
		for (organization org : orgRepo.findAll()) {
			orgs.add(org);
		}
		return orgs;
	}
}
