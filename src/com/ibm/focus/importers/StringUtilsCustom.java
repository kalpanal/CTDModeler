package com.ibm.focus.importers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

public class StringUtilsCustom {
	
	public static String getDataSheetName(String dataSourceFullPath) throws Exception {
		String sheetName = null;
		if(dataSourceFullPath != null || (!dataSourceFullPath.isEmpty())){
			ArrayList<Integer> listofPositionsOfColon = occurrencesPos(dataSourceFullPath, ":");
			sheetName = dataSourceFullPath.substring(dataSourceFullPath.indexOf(":", listofPositionsOfColon.get(1))+1);
		}
		
		return sheetName;
	}
	/*C:\Kalpana\TD Bank\datasheets\DocDelivery.xlsx: Pos-printing;3 */
	public static String getFileNameWithPath(String fullPathWithRowIndex) throws Exception {
		String fullPathWithOutRowIndex = null;
		if((fullPathWithRowIndex != null || (!fullPathWithRowIndex.isEmpty())) && (fullPathWithRowIndex.indexOf(";") > 0)) {
			fullPathWithOutRowIndex = fullPathWithRowIndex.substring(0,fullPathWithRowIndex.indexOf(";"));
		}else{
			fullPathWithOutRowIndex = fullPathWithRowIndex;
		}
		
		return fullPathWithOutRowIndex;
	}


	public static String getFileNameWithOutSheet(String fullpathWithSheetName) throws Exception {
		String fullPathWithOutSheetName = null;
		if(fullpathWithSheetName != null || (!fullpathWithSheetName.isEmpty())){
			ArrayList<Integer> listofPositionsOfColon = occurrencesPos(fullpathWithSheetName, ":");
			fullPathWithOutSheetName = fullpathWithSheetName.substring(0,fullpathWithSheetName.indexOf(":", listofPositionsOfColon.get(1)));
		}
		
		return fullPathWithOutSheetName;
	}
	
	public static String getSheetNameAlone(String fullpathWithSheetName) throws Exception {
		String fullPathWithOutSheetName = null;
		if(fullpathWithSheetName != null || (!fullpathWithSheetName.isEmpty())){
			fullPathWithOutSheetName = fullpathWithSheetName.substring(fullpathWithSheetName.indexOf(":")+1);
		}
		
		return fullPathWithOutSheetName.trim();
	}
	
	
	public static ArrayList<Integer> occurrencesPos(String str, String substr) {
	    final boolean ignoreCase = true;
	    int substrLength = substr.length();
	    int strLength = str.length();

	    ArrayList<Integer> occurrenceArr = new ArrayList<Integer>();

	    for(int i = 0; i < strLength - substrLength + 1; i++) {
	        if(str.regionMatches(ignoreCase, i, substr, 0, substrLength))  {
	            occurrenceArr.add(i);
	        }
	    }
	    return occurrenceArr;
	}
}
