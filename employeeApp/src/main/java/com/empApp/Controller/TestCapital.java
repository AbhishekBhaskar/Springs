package com.empApp.Controller;

public class TestCapital {
	public static String toCamelCase(String s)
	{
		
		char firstChar = s.charAt(0);
		char fc = Character.toUpperCase(firstChar);
		String str = fc + "";
		for(int i=1;i<s.length();i++)
		{
			str += s.charAt(i);
		}
		
		/*
		 * for(int i=0;i<str.length()-1;i++) {
		 * 
		 * if(str.charAt(i) == '_') {
		 * 
		 * char ch = str.charAt(i+1);
		 * 
		 * str = str.replace(str.charAt(i), '\0'); str = str.replace(ch,
		 * Character.toUpperCase(ch)); }
		 * 
		 * }
		 */
		
		str = str.replace("_", "");
		return str;
	}
	 public static void main(String args[])
	 {
		 String s = "Candidate_detailsRepository";
		 String converted = toCamelCase(s);
		 System.out.println(converted);
	 }

}
