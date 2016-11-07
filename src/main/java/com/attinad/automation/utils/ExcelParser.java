package com.attinad.automation.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelParser {

	public static XSSFSheet readDetails(String path) throws IOException {
		XSSFSheet sheet = null;
		if(!path.isEmpty()){
		InputStream fis = new FileInputStream(new File(path));

		// Create a workbook instance that refers to excel file
		XSSFWorkbook wb = new XSSFWorkbook(fis);

		// Create a sheet object to retrieve the sheet
		 sheet = wb.getSheetAt(0);
	}
		return sheet;
	
}
		

	public static Object getCellValue(Cell cell) throws NullPointerException {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();

		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue();

		case Cell.CELL_TYPE_NUMERIC:
			return cell.getNumericCellValue();

		case Cell.CELL_TYPE_BLANK:
			return null;
		case Cell.CELL_TYPE_FORMULA:
			return cell.getCellFormula();

		}
		return null;
	}
}
