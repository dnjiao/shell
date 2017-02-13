package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import models.Settlement;
import models.VolPricePair;

public class SunocoXls {
	public static void readSettlementXls(String inXls, String outXls) throws IOException {
		File inFile = new File(inXls);
		XSSFWorkbook inBook = new XSSFWorkbook(new FileInputStream(inFile));
		FileInputStream inp = new FileInputStream(outXls);
		XSSFWorkbook outBook = new XSSFWorkbook(inp);
		for (int i = 0; i < inBook.getNumberOfSheets(); i++){
			
		
			XSSFSheet inSheet = inBook.getSheetAt(i);
			Row inRow = null;
			Cell inCell = null;
			for (int rowId = 0; rowId <= inSheet.getLastRowNum(); rowId++)
	    	{
				inRow = inSheet.getRow(rowId);
				inCell = inRow.getCell(0);
				if (inCell == null) {
					continue;
				} else if (!inCell.getStringCellValue().equalsIgnoreCase("1")) {
					continue;
				}
				double volume = inRow.getCell(3).getNumericCellValue();
				double price = inRow.getCell(4).getNumericCellValue();
				
				
				Settlement settlement = new Settlement();
				cell = row.getCell(colNameMap.get("Contract#"));
	    	}
		}
	}
}
