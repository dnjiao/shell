package app;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import models.Settlement;
import util.SettlementXls;
import util.SunocoXls;

public class ShellExe {

	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter path to working directory:");
		String inputPath = sc.nextLine();
		System.out.println("Enter the filename of first spreadsheet:");
		String xls1 = sc.nextLine();
		Path path1 = Paths.get(inputPath, xls1);
		String xlsPath1 = path1.toString();
		System.out.println("Input 1: " + xlsPath1);
		System.out.println("Now reading " + xlsPath1);
		List<List<Settlement>> lists = SettlementXls.readShellXls(xlsPath1);
		System.out.println("Parsing completed for " + xlsPath1);
		System.out.println("Now appending tabs to " + xlsPath1);
		SettlementXls.writeShellXls(lists, xlsPath1);
		System.out.println("Update completed for " + xlsPath1);
		System.out.println("\n\nProceed with second spreadsheet? [Y/N]");
		char yesOrNo = sc.nextLine().charAt(0);
		if (yesOrNo == 'N' || yesOrNo == 'n') {
			System.out.println("End of execution");
			System.exit(0);
		}
		System.out.println("Enter the filename of the second spreadsheet:");
		
		String xls2 = sc.nextLine();
		System.out.println(xls2);
		
		Path path2 = Paths.get(inputPath, xls2);
		String xlsPath2 = path2.toString();
		System.out.println("Input 2: " + xlsPath2);
		System.out.println("Now updating spreadsheet in " + xlsPath2);
		SunocoXls.processSettlementXls(xlsPath2, lists);
		System.out.println("Update completed for " + xlsPath2);
		
		

	}

}
