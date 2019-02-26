package com.automate.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class Test {

	public static void main(String[] args) throws Exception{
		/*
		 * Process p=Runtime.getRuntime().
		 * exec("cmd.exe /c mvn clean install com.jaxio.celerio:bootstrap-maven-plugin:bootstrap"
		 * ,null,new
		 * File("C:\\AutoCodeGeneration\\ivu.apie.database\\ivu.apie.database"));
		 * copy(p.getInputStream(), System.out); p.waitFor();
		 */
		
		String line;
		//Scanner scan = new Scanner(System.in);

		Process process = Runtime.getRuntime ().exec("cmd.exe /c mvn clean install com.jaxio.celerio:bootstrap-maven-plugin:bootstrap",null,
				new File("C:\\AutoCodeGeneration\\ivu.apie.database\\ivu.apie.database"));
		OutputStream stdin = process.getOutputStream ();
		InputStream stderr = process.getErrorStream ();
		InputStream stdout = process.getInputStream ();

		BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));

		String input = "2";//scan.nextLine();
		input += "\n";
		writer.write(input);
		writer.flush();

		input = "4";
		input += "\n";
		writer.write(input);
		writer.flush();

		/*
		 * while ((line = reader.readLine ()) != null) { System.out.println ("Stdout: "
		 * + line); }
		 */

		input = "vvvvvv";
		//input = "vvvvvv";
		input += "\n";
		writer.write(input);
		writer.flush();
		
		input = "com.jaxio.vvvvvv";
		//input = "package_name";
		input += "\n";
		writer.write(input);
		writer.close();

		while ((line = reader.readLine ()) != null) {
		System.out.println ("Stdout: " + line);
		}
	}
	
	static void copy(InputStream in, OutputStream out) throws IOException {
	    while (true) {
	        int c = in.read();
	        if (c == -1)
	            break;
	        out.write((char) c);
	    }
	}
}
