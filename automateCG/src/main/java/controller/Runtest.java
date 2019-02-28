package com.automate.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.automate.services.UserService;

public class Runtest {

	static void replace(String link, String newContent, String oldContent) {
		File f = new File(link);
		StringBuffer old = new StringBuffer();
		// System.out.println("Enter application name");
		// BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader reader = null;
		FileWriter writer = null;
		// app_name=br.readLine();

		try {
			reader = new BufferedReader(new FileReader(f));

			// Reading all the lines of input text file into oldContent

			String line = null;

			while ((line = reader.readLine()) != null) {
				old.append(line + System.lineSeparator());

				// line = reader.readLine();
			}
			reader.close();
			// Pattern ptn=Pattern.compile("jdbc:mysql://.*");
			Pattern ptn = Pattern.compile(oldContent);
			Matcher match = ptn.matcher(old);
			String replaced = match.replaceAll(newContent);
			writer = new FileWriter(f);
			writer.write(replaced);
			writer.flush();
			writer.close();
		} catch (IOException e) {
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
	
	static void copy(InputStream in, OutputStream out) throws IOException {
		while (true) {
			int c = in.read();
			if (c == -1)
				break;
			out.write((char) c);
		}
	}

	public static void main(String args[]) throws IOException {
		
		String line3;
		Process process = Runtime.getRuntime ().exec("cmd.exe /c mvn -Pmetadata,gen generate-sources",null,new File("C:\\Users\\Abhi\\Desktop\\abhi\\springs\\github_files\\Springs-master\\AutoCodeGeneration\\ivu.apie.database\\automate"));
		OutputStream stdin = process.getOutputStream ();
		InputStream stderr = process.getErrorStream ();
		InputStream stdout = process.getInputStream ();
		
		BufferedReader reader3 = new BufferedReader (new InputStreamReader(stdout));
		BufferedWriter writer3 = new BufferedWriter(new OutputStreamWriter(stdin));
		
		while ((line3 = reader3.readLine ()) != null) {
			System.out.println ("Stdout: " + line3);
			}   
		
		

		System.out.println("Done");
		
	}
}
