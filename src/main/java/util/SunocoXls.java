package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import models.Pivot;
import models.Settlement;
import models.VolPricePair;

public class SunocoXls {
	public static List<List<Pivot>> readSettlementXls(String inXls) throws IOException {
		File inFile = new File(inXls);
		XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(inFile));
		XSSFSheet sheet = workbook.getSheetAt(0);
		int readFlag = 0;

		for (int rowId = 0; rowId < sheet.getLastRowNum(); rowId++) {
			Row row = sheet.getRow(rowId);
			if (row.getCell(0) != null) {
				if (row.getCell(0).getStringCellValue().toLowerCase().startsWith("account"))) {
					readFlag = 1;
					continue;
				}
			}
			if (readFlag == 1) {
				Pivot pivot = new Pivot();
				pivot.setAccount(row.getCell(0).getStringCellValue());
				pivot.setInvoice(row.getCell(1).getStringCellValue());
				pivot.setVolume(row.getCell(2).getNumericCellValue());
				pivot.setPrice(row.getCell(3).getNumericCellValue());
			}
		}
		
		while (cell )
		for (int i = 0; i < inBook.getNumberOfSheets(); i++){
			String sheetName = inBook.getSheetName(i).toLowerCase();
			XSSFSheet inSheet = inBook.getSheetAt(i);
			XSSFSheet outSheet;
			List<VolPricePair> volPrice;
			if (sheetName.contains("bulk") && sheetName.contains("ar")) {
				outSheet = outBook.createSheet("BULK AR");
				volPrice = vpPairs.get(0);
			}  else if (sheetName.contains("lease") && sheetName.contains("ar")) {
				outSheet = outBook.createSheet("LEASE AR");
				volPrice = vpPairs.get(1);
			} else if (sheetName.contains("bulk") && sheetName.contains("ap")) {
				outSheet = outBook.createSheet("LEASE AR");
				volPrice = vpPairs.get(2);
			} else if (sheetName.startsWith("royalty")) {
				outSheet = outBook.createSheet("Royalty");
				volPrice = vpPairs.get(3);
			} else {
				continue;
			}
		
			Row inRow = null;
			Row outRow = null;
			Cell inCell = null;
			Cell outCell = null;
			int outRowId = 0;
			
			double epsilon = 0.001;
			for (int inRowId = 0; inRowId <= inSheet.getLastRowNum(); inRowId++)
	    	{
				inRow = inSheet.getRow(inRowId);
				inCell = inRow.getCell(0);
				if (inCell != null) {
					if (inCell.getCellTypeEnum() == CellType.NUMERIC) {
						outRow = outSheet.createRow(outRowId++);
						outCell = outRow.createCell(0);
						outCell.setCellValue((int)inCell.getNumericCellValue());
						outCell= outRow.createCell(1);
						outCell.setCellValue(inRow.getCell(1).getStringCellValue());
						outCell= outRow.createCell(2);
						outCell.setCellValue(inRow.getCell(2).getStringCellValue());
						double volume = inRow.getCell(3).getNumericCellValue();
						outCell = outRow.createCell(3);
						outCell.setCellValue(volume);
						double price = inRow.getCell(4).getNumericCellValue();
						outCell = outRow.createCell(4);
						outCell.setCellValue(price);
						outCell = outRow.createCell(5);
						outCell.setCellValue(price / volume);
						
						int j;
						for (j = 0; j < volPrice.size(); j++) {
							double shellVolume = volPrice.get(j).getVolume();
							double shellPrice = volPrice.get(j).getPrice();
							if (Math.abs(shellVolume - volume) < epsilon && Math.abs(shellPrice - price) < epsilon) {
								outCell = outRow.createCell(6);
								outCell.setCellValue(shellVolume);
								outCell = outRow.createCell(7);
								outCell.setCellValue(shellPrice);
								outCell = outRow.createCell(8);
								outCell.setCellValue(shellPrice / shellVolume);
								break;
							}
						}
						volPrice.remove(j);
						
					}
				}
				
				// If not all volPrice matched, print leftover at the bottom
				if (volPrice.size() > 0) {
					for (VolPricePair pair : volPrice) {
						outRow = outSheet.createRow(outRowId++);
						outCell = outRow.createCell(6);
						outCell.setCellValue(pair.getVolume());
						outCell = outRow.createCell(7);
						outCell.setCellValue(pair.getPrice());
						outCell = outRow.createCell(8);
						outCell.setCellValue(pair.getPrice() / pair.getVolume());
					}
				}
				
				
	    	}
		}
	}
}
