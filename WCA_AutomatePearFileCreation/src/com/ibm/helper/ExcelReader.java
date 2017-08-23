package com.ibm.helper;

import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class ExcelReader {

	@SuppressWarnings("unchecked")
	//    public static void main(String[] args) throws Exception {
	private List readExcel(String filename_with_path, String sheetName){	
		// An excel file name. You can create a file name with a full path information.

		// Create an ArrayList to store the data read from excel sheet.
		List sheetData = new ArrayList();

		FileInputStream fis = null;
		try {
			// Create a FileInputStream that will be use to read the excel file.
			fis = new FileInputStream(filename_with_path);

			// Create an excel workbook from the file system.
			XSSFWorkbook workbook = new XSSFWorkbook(fis);

			// Get the first sheet of the workbook.
			XSSFSheet sheet = workbook.getSheetAt(0);

			// When we have a sheet object in hand we can iterator on each
			// sheet's rows and on each row's cells. We store the data read
			// on an ArrayList so that we can printed the content of the excel
			// to the console.

			Iterator rows = null;
			if (sheetName.equalsIgnoreCase("Sheet1")){
				rows = sheet.rowIterator();
			}

			while (rows.hasNext()) {
				XSSFRow row = (XSSFRow) rows.next();
				int colNum = row.getLastCellNum();

				List data = new ArrayList();
				for (int i=0; i<colNum; i++) {
					XSSFCell cell = row.getCell(i,MissingCellPolicy.CREATE_NULL_AS_BLANK);
					data.add(cell);
				}

				sheetData.add(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return sheetData;
	}

	public ArrayList<String> getRegExWordsList(String filename_with_path, String sheetName){

		String regExWords = null;
		ArrayList<String> regexWordsList = new ArrayList<String>();
		List sheetData;
		try {
			sheetData = readExcel(filename_with_path,sheetName);
			for (int i = 1; i < sheetData.size(); i++) {// i=1 as discarding the first row
				List list = (List) sheetData.get(i);
				XSSFCell regExCell = (XSSFCell) list.get(0);
				regExWords = regExCell.getRichStringCellValue().getString();
				//System.out.println("regExWords : "+regExWords);

				regexWordsList.add(regExWords);                       
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return regexWordsList;
	}


	public ArrayList<String> getAnnotatorTypeList(String filename_with_path,String sheetName){

		String annotatorType = null;
		ArrayList<String> annotatorTypeList = new ArrayList<String>();
		List sheetData;
		try{
			sheetData = readExcel(filename_with_path,sheetName);
			for (int i = 1; i < sheetData.size(); i++) {// i=1 as discarding the first row
				List list = (List) sheetData.get(i); // Get all the columns in that row

				XSSFCell annotatorTypeCell = (XSSFCell) list.get(list.size()-1); // Make sure that the Annotator Type names are always the last column of the sheet
				annotatorType = annotatorTypeCell.getRichStringCellValue().getString();
				//System.out.println("annotatorType : "+annotatorType);


				annotatorTypeList.add(annotatorType); 
			}                     
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
		return annotatorTypeList;
	}


}