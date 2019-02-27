package com.automate.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.automate.services.UserService;

public class replaceTest {
	
	
	private static UserService userService;
	
	static void replace(String link, String newContent, String oldContent) {
		File f = new File(link);
		String old = "";
		// System.out.println("Enter application name");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader reader = null;
		FileWriter writer = null;
		// app_name=br.readLine();

		try {
			reader = new BufferedReader(new FileReader(f));

			// Reading all the lines of input text file into oldContent

			String line = reader.readLine();

			while (line != null) {
				old = old + line + System.lineSeparator();

				line = reader.readLine();
			}
			// Pattern ptn=Pattern.compile("jdbc:mysql://.*");
			Pattern ptn = Pattern.compile(oldContent);
			Matcher match = ptn.matcher(old);
		String	replaced = match.replaceAll(newContent);
			writer = new FileWriter(f);
			writer.write(newContent);
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
	
	public static void main(String args[])
	{
		

	String schema_name="schema_name";
	String host="host_name";
	String uname="uname";
	String passwd = "password";
	String package_name="pack_name";
	
	
	String link="C:\\AutoCodeGeneration\\ivu.apie.database\\ivu.apie.database\\src\\main\\resources\\celerio\\pack-backend-jpa\\bootstrap\\pom.boot.vm.xml";
	String new_dbname="<database.name>" + schema_name + "</database.name>";
	String new_dbhost="<database.host>" + host + "</database.host>";
	String new_dbuser="<database.user>" + uname + "</database.host>";
	String new_dbpwd="<database.password>" + passwd + "</database.password>";
	String new_jdbc_url="<jdbc.url>jdbc:mysql://" + host + ":3306/" + schema_name + "</jdbc.user>";
	String new_jdbc_user="<jdbc.user>" + uname + "</jdbc.user>";
	String new_jdbc_passwd="<jdbc.user>" + passwd + "</jdbc.user>";
	
	String old_dbname="<database\\.name>.*";
	String old_dbhoat="<database\\.host>.*";
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
	
	replace(link, new_dbname, old_dbname);
	replace(link, new_dbhost, old_dbhoat);
	replace(link, new_dbuser, old_dbuser);
	replace(link, new_dbpwd, old_dbpassword);
	replace(link, new_jdbc_url, old_jdbc_url);
	replace(link, new_jdbc_user, old_jdbc_user);
	replace(link2, new_pack_name1, old_pack_name1);
	replace(link2, new_pack_name2, old_pack_name2);
	replace(link2, new_pack_name3, old_pack_name3);
	
	System.out.println("replaced");
}

}
