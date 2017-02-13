package util;

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
import models.VolPricePair;

public class SettlementXls {
	final static Logger logger = Logger.getLogger(SettlementXls.class);
	
	public static List<List<VolPricePair>> readSettlementXls(String xlsPath) throws IOException {
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
				settlement.setBuySellFlag(cell.getStringCellValue());
			}
			cell = row.getCell(colNameMap.get("Location"));
			if (cell != null) {
				settlement.setLocation(cell.getStringCellValue());
			}
			cell = row.getCell(colNameMap.get("Lease#"));
			if (cell != null) {
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
			
			if (settlement.getBuySellFlag().equalsIgnoreCase("Buy")) {
				if (settlement.getLeaseName() == null || settlement.getLeaseName().equalsIgnoreCase("NA")) {
					bulkSA.add(settlement);
				} else {
					leaseSA.add(settlement);
				}
			} else if (settlement.getBuySellFlag().equalsIgnoreCase("Sell")) {
				if (settlement.getLocation().endsWith("Lease Sale")) {
					leasePA.add(settlement);
				} else {
					bulkPA.add(settlement);
				}
			} else {
				System.out.println("ERROR in row " + Integer.toString(rowId) + " invalid value in Buy/Sell Flag column.");
			}
    	}
		Map<String, List<Settlement>> bsMap = groupBySmartNo(bulkSA);
		Map<String, List<Settlement>> lsMap = groupBySmartNo(leaseSA);
		Map<String, List<Settlement>> bpMap = groupBySmartNo(bulkPA);
		Map<String, List<Settlement>> lpMap = groupBySmartNo(leasePA);
		List<VolPricePair> bsPair = summingAmount(bsMap);
		List<VolPricePair> lsPair = summingAmount(lsMap);
		List<VolPricePair> bpPair = summingAmount(bpMap);
		List<VolPricePair> lpPair = summingAmount(lpMap);
		
		List<List<VolPricePair>> masterList = new ArrayList<List<VolPricePair>>(4);
		masterList.add(bsPair);
		masterList.add(lsPair);
		masterList.add(bpPair);
		masterList.add(lpPair);
		return masterList;
	}
	
	public static Map<String, List<Settlement>> groupBySmartNo(List<Settlement> list) {
		Map<String, List<Settlement>> map = new HashMap<String, List<Settlement>>();
		map = list.stream().collect(Collectors.groupingBy(Settlement::getSmartNo));
		return map;
	}
	
	public static List<VolPricePair> summingAmount(Map<String, List<Settlement>> map) {
		List<VolPricePair> vpList = new ArrayList<VolPricePair>();
		for (Map.Entry<String, List<Settlement>> entry : map.entrySet()) {
			VolPricePair vp = new VolPricePair();
		    double total = 0.0;
		    for (Settlement sett : entry.getValue()) {
		    	total += sett.getSettleAmount();
		    	vp.setVolume(sett.getVolume());
		    }
		    vp.setPrice(total);
		    vpList.add(vp);
		}
		return vpList;
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

	
