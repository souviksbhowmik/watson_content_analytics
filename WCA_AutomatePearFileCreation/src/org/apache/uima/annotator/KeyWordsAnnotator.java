package org.apache.uima.annotator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.*;

import com.ibm.helper.ExcelReader;
import com.ibm.helper.RegExConverter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;

public class KeyWordsAnnotator {

	ExcelReader excelReader = new ExcelReader();
	RegExConverter regExConverter = new RegExConverter();

	private Pattern regExWords;


	public void annotateKeyWords (JCas aJCas) throws AnalysisEngineProcessException {
		// The JCas object is the data object inside UIMA where all the
		// information is stored. It contains all annotations created by
		// previous annotators, and the document text to be analyzed.

		// get document text from JCas
		String docText = aJCas.getDocumentText();

		Properties prop = new Properties();
		try {
			InputStream input = new FileInputStream("C:\\Users\\IBM_ADMIN\\workspace\\WCA_AutomatePearFileCreation\\resources\\config.properties");
			prop.load(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String filename = prop.getProperty("excelFilePath")+prop.getProperty("excelFileName");
		String sheetName = prop.getProperty("sheet1");

		List regex_words_list = excelReader.getRegExWordsList(filename,sheetName);
		// System.out.println(regex_words_list);

		List annotatorType_list = excelReader.getAnnotatorTypeList(filename,sheetName);
		// System.out.println(annotatorType_list);

		for (int i = 0; i < regex_words_list.size(); i++) {

			String regExWords_from_list = regex_words_list.get(i).toString();
			/*
			 * Convert the regex which was extracted from excel sheet to make sure whole word gets matched.
			 * For example if not converted, regex pattern "car" will find "car" in "credit card" also.
			 * However, out intention is to match "car" when "car" is a full word match. Such as "car" in "car parts".
			 */
			String regExWords_Converted = regExConverter.convertRegEx(regExWords_from_list); 
			regExWords = Pattern.compile(regExWords_Converted);
			//System.out.println(regExWords);

			// search for RegEx words
			Matcher regEx_matcher = regExWords.matcher(docText);
			int pos = 0;
			while (regEx_matcher.find(pos)) {
				// match found - create the match as annotation in
				// the JCas with some additional meta information

				String annotatorType = annotatorType_list.get(i).toString();
				//System.out.println("annotatorType :"+annotatorType);
				try {

					Class c = Class.forName("org.apache.uima."+annotatorType);
					Annotation annotation = (Annotation) c.getDeclaredConstructor(JCas.class).newInstance(aJCas);
					Method[] allMethods = c.getDeclaredMethods();

					annotation.setBegin(regEx_matcher.start());
					annotation.setEnd(regEx_matcher.end());
					annotation.addToIndexes();

					pos = regEx_matcher.end();// Shift the matcher position to the end of the RegEx match

				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 

			}
		}
	}
}








