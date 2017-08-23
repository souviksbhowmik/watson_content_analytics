package com.ibm.helper;

import java.util.StringTokenizer;

public class RegExConverter {
	
	private static final String delimiter = "|";
	public String convertRegEx(String regEx){
		
		String convertedRegEx = "";
		
		StringTokenizer strTok = new StringTokenizer(regEx, delimiter);
		while(strTok.hasMoreTokens()){
			convertedRegEx = convertedRegEx + "\\b" + strTok.nextToken() + "\\b" + delimiter;
		}
		
		if (convertedRegEx.length() > 0) {
			convertedRegEx = convertedRegEx.substring(0, convertedRegEx.length() -1);
		}
		return convertedRegEx;
	}
	
}
