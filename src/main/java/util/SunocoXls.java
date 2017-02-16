package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import models.Settlement;

public class SunocoXls {
	public static void processSettlementXls(String path, List<List<Settlement>> lists) throws IOException {
		FileInputStream input = new FileInputStream(path);
		XSSFWorkbook workbook = new XSSFWorkbook(input);
		XSSFSheet sheet = null;
		List<Settlement> settlements = null;
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			// find corresponding list for each tab
			String sheetName = workbook.getSheetName(i).toLowerCase().trim();	
			sheet = workbook.getSheetAt(i);
			if (sheetName.contains("bulk") && sheetName.contains("ar")) {
				settlements = lists.get(0);
			} else if (sheetName.contains("lease") && sheetName.contains("ar")) {
				settlements = lists.get(1);
			} else if (sheetName.contains("bulk") && sheetName.contains("ap")) {
				settlements = lists.get(2);
			} else {
				break;
			}	
			
			double epsilon = 0.001;
			int rowId;
			for (rowId = 6; rowId <= sheet.getLastRowNum(); rowId++)
	    	{
				Row row = sheet.getRow(rowId);
				if (row.getCell(1) == null) {
					break;
				} else if (row.getCell(1).getStringCellValue() == "") {
					break;
				}
				if (row.getCell(3) == null || row.getCell(4) == null) {
					continue;
				}
				double volume = row.getCell(3).getNumericCellValue();
				double price = row.getCell(4).getNumericCellValue();
				Iterator<Settlement> iter = settlements.iterator();
				while (iter.hasNext()) {
					Settlement s = iter.next();
					double shellVolume = s.getVolume();
					double shellPrice = s.getSettleAmount();
					if (Math.abs(shellVolume - volume) < epsilon && Math.abs(shellPrice - price) < epsilon) {
						row.getCell(7).setCellValue(shellVolume);
						row.getCell(8).setCellValue(shellPrice);
						double base = shellPrice / shellVolume;
						row.getCell(9).setCellValue(base);
						iter.remove();
						break;
					}
				}
	    	}
			// skip the summary line
			rowId += 3;
			// If not all matched, append leftover at the bottom
			if (settlements.size() > 0) {
				for (Settlement sett : settlements) {
					Row row = sheet.createRow(rowId++);
					Cell cell = row.createCell(7);
					cell.setCellValue(sett.getVolume());
					cell = row.createCell(8);
					cell.setCellValue(sett.getSettleAmount());
					cell = row.createCell(9);
					cell.setCellValue(sett.getSettleAmount() / sett.getVolume());
				}
			}
		}
		input.close();
		
		//update xls
		FileOutputStream outFile =new FileOutputStream(path);
        workbook.write(outFile);
        workbook.close();
        outFile.close();
	}
}
