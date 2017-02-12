package accounting;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import models.Settlement;

public class XlsUtils {
	final static Logger logger = Logger.getLogger(XlsUtils.class);
	
	public static List<List<Settlement>> readSettlementXls(String xlsPath) throws IOException {
		List<List<Settlement>> masterList = new ArrayList<List<Settlement>>(4);
		File inFile = new File(xlsPath);
		Workbook workbook = new XSSFWorkbook(new FileInputStream(inFile));
		Sheet sheet = workbook.getSheetAt(0);
		Row row = sheet.getRow(0);
		Cell cell = null;
		Map<String, Integer> colNameMap = getColNameMap(row);
		List<Settlement> bulkSA = new ArrayList<Settlement>();
		List<Settlement> bulkPA = new ArrayList<Settlement>();
		List<Settlement> leaseSA = new ArrayList<Settlement>();
		List<Settlement> leasePA = new ArrayList<Settlement>();
		for (int rowId = 1; rowId <= sheet.getLastRowNum(); rowId++)
    	{
			int buySellFlag = -1;
			int bulkLeaseFlag = -1;
			row = sheet.getRow(rowId);
			Settlement settlement = new Settlement();
			cell = row.getCell(colNameMap.get("Contract#"));
			if (cell != null) {
				settlement.setContractNo(cell.getStringCellValue());
			}
			cell = row.getCell(colNameMap.get("Smart#"));
			if (cell != null) {
				settlement.setSmartNo(cell.getStringCellValue());
			}
			cell = row.getCell(colNameMap.get("DealTracking #"));
			if (cell != null) {
				settlement.setDealTrackNo(cell.getStringCellValue());
			}
			cell = row.getCell(colNameMap.get("STA Netting\nBuy/Sell Flag"));
			if (cell != null) {
				String str = cell.getStringCellValue();
				if (str.equalsIgnoreCase("buy")) {
					buySellFlag = 0;
				} else if (str.equalsIgnoreCase("Sell")) {
					buySellFlag = 1;
				}
				settlement.setBuySellFlag(str);
			}
			cell = row.getCell(colNameMap.get("Location"));
			if (cell != null) {
				settlement.setLocation(cell.getStringCellValue());
			}
			cell = row.getCell(colNameMap.get("Lease#"));
			if (cell == null) {
				bulkLeaseFlag = 0;
			}
			else {
				bulkLeaseFlag = 1;
				settlement.setLeaseNo(cell.getStringCellValue());
			}
			cell = row.getCell(colNameMap.get("Lease\nName"));
			if (cell != null) {
				settlement.setLeaseName(cell.getStringCellValue());
			}
			cell = row.getCell(colNameMap.get("BAVVolume"));
			if (cell != null) {
				settlement.setVolume(cell.getNumericCellValue());
			}
			cell = row.getCell(colNameMap.get("Price"));
			if (cell != null) {
				settlement.setPrice(cell.getNumericCellValue());
			}
			cell = row.getCell(colNameMap.get("CurrentSettle Amount"));
			if (cell != null) {
				settlement.setSettleAmount(cell.getNumericCellValue());
			}
			
			if (buySellFlag == 0) {
				if (bulkLeaseFlag == 0) {
					bulkPA.add(settlement);
				} else {
					leasePA.add(settlement);
				} 
			} else if (buySellFlag == 0) {
				if (bulkLeaseFlag == 0) {
					bulkSA.add(settlement);
				} else {
					leaseSA.add(settlement);
				} 
			} else {
				System.out.println("ERROR in row " + Integer.toString(rowId) + " invalid value in Buy/Sell Flag column.");
			}
    	}
		masterList.add(bulkSA);
		masterList.add(leaseSA);
		masterList.add(bulkPA);
		masterList.add(leasePA);
		return masterList;
	}
	
	public static List<List<Settlement>> groupBySmartNo(List<List<Settlement>> lists) {
		List<List<Settlement>> newLists = new ArrayList<List<Settlement>>(4);
		for (List<Settlement> list : lists) {
			Map<String, List<Settlement>> map = new HashMap<String, List<Settlement>>();
			map = list.stream().collect(Collectors.groupingBy(Settlement::getSmartNo));
			
		}
	}

	private static Map<String, Integer> getColNameMap(Row row) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		short colTotal = row.getLastCellNum();
		for (int i = 0; i < colTotal; i++) {
			Cell cell =row.getCell(i);
			map.put(cell.getStringCellValue(),cell.getColumnIndex());
			
		}
		return map;
	
	}
	
}

	
