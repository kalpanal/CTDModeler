package com.ibm.focus.importers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.util.IteratorIterable;



public class ReadExcelDataSheet {
	private static final String FILE_NAME = "C:/Kalpana/TD Bank/datasheets/DocDelivery.xlsx";

	public static void buildDataSources(Element testSuiteMain, String appConfigPath) throws FileNotFoundException, Exception {
		
		IteratorIterable<Content> descendantsOfChannel = testSuiteMain.getDescendants();
		Element dataSourcesSize = null;
		ArrayList<AppConfigurationPropertiesForDataSheet> appConfigPropertiesForDatasheets = Util.loadPropertiesForDataSheets(appConfigPath);
		String dataSheetPath = Util.loadProperties("DATA_SOURCE_PATH", appConfigPath);
		
		for (Content descendant : descendantsOfChannel) {
			if (descendant.getCType().equals(Content.CType.Element)) {
				Element child = (Element) descendant;
				 if(child.getName().equalsIgnoreCase("dataSourcesSize")){
					dataSourcesSize = child;
										
				}
			}
		}
		
		dataSourcesSize.removeContent();
		dataSourcesSize.addContent((appConfigPropertiesForDatasheets.size())+"");
		for (AppConfigurationPropertiesForDataSheet appConfigurationPropertiesForDataSheet : appConfigPropertiesForDatasheets) {
			try {
				//inputExcelFileVO = parse(configurationTO.getDataSourcePath()+"/"+dataSheetName);
				Element dataSource = new Element("DataSource");
				dataSource.setAttribute("className", "com.parasoft.data.DataSource");
				dataSource.setAttribute("version", "1.20");
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				dataSource.addContent(new Element("id").addContent("ds_674270746_"+timestamp.getTime()));
				
				Element excelDataSourceImpl = new Element("ExcelDataSourceImpl");
				excelDataSourceImpl.setAttribute("className", "com.parasoft.data.ExcelDataSourceImpl");
				excelDataSourceImpl.setAttribute("version", "1.5.5");
				System.out.println("appConfigurationPropertiesForDataSheet.getDatasheetName()"+appConfigurationPropertiesForDataSheet.getDatasheetName());
				
				Element name = new Element("name");
				name.addContent(StringUtilsCustom.getSheetNameAlone(appConfigurationPropertiesForDataSheet.getDatasheetName()));
				
				Element sheets = generateDatasheets(dataSheetPath+"/"+appConfigurationPropertiesForDataSheet.getDatasheetName());
				excelDataSourceImpl.addContent(sheets);
				
				dataSource.addContent(excelDataSourceImpl);
				dataSource.addContent(name);
				
				
				dataSourcesSize.getParentElement().addContent(34, dataSource);
				
				//dataSourcesSize
			} catch (Exception e) {
				System.out.println("Error while generation datasource XML nodes"+e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private static Element generateDatasheets(String fileNameWithPath) throws FileNotFoundException, Exception {		
		
		System.out.println("StringUtilsCustom.getFileNameWithOutSheet(fileNameWithPath)====>"+fileNameWithPath);
		FileInputStream spreadsheet = new FileInputStream(new File(
				StringUtilsCustom.getFileNameWithOutSheet(fileNameWithPath)));
		
		XSSFWorkbook workbook = new XSSFWorkbook(spreadsheet);
		Element sheets = new Element("sheets");
		sheets.setAttribute("size", (workbook.getNumberOfSheets()-1)+"");
		int indexNum = 0;
		for (int i = 0; i< workbook.getNumberOfSheets() - 1; i++) {
            XSSFSheet individualSheet = workbook.getSheetAt(i);
            InputExcelFileVO inputExcelFileVO = parse(StringUtilsCustom.getFileNameWithOutSheet(fileNameWithPath)+":"+individualSheet.getSheetName());
            inputExcelFileVO.getHeaders();
            Element sheetName = new Element("sheetName");
            sheetName.setAttribute("index", indexNum+"");
            sheetName.addContent(individualSheet.getSheetName());
            
            Element data  = new Element("data"); 
            data.setAttribute("index", indexNum+"");
            data.addContent("true");
            
            Element columnNames = new Element("columnNames");
            
            int j=0;
            for(String columnNameStr : inputExcelFileVO.getHeaders()) {
            	if(!columnNameStr.equals("")){
		               Element columnName = new Element("columnName");
		               columnName.setAttribute("index", j+"");
		               j++;
		               columnName.addContent(columnNameStr);
		               columnNames.addContent(columnName);
            	}
           }
            
            columnNames.setAttribute("size", j+"");
            columnNames.setAttribute("index", indexNum+"");
            indexNum++;
            sheets.addContent(sheetName);
            sheets.addContent(data);
            sheets.addContent(columnNames);
            
        }
		System.out.println("StringUtilsCustom.getFileNameWithOutSheet(fileNameWithPath)==sheets====>"+workbook.getNumberOfSheets());
		
		return sheets;
		
	}

	public static InputExcelFileVO parse(String fileNameWithPath)
			throws Exception {
		try {
			FileInputStream spreadsheet = new FileInputStream(new File(StringUtilsCustom.getFileNameWithOutSheet(fileNameWithPath)));
			XSSFWorkbook workbook = new XSSFWorkbook(spreadsheet);
			
			// Get first/desire sheed from the workbook

			XSSFSheet sheet = workbook.getSheet(StringUtilsCustom.getDataSheetName(
					fileNameWithPath).trim());
			Iterator<Row> rows = sheet.rowIterator();

			List<String> headers = new ArrayList<>();
/*			DataFormatter formatter = new DataFormatter();
			if (rows.hasNext()) {
				Row row = rows.next();
				for (Cell cell : row) {
					headers.add(getCellValue(cell));
				}
			}*/
			
			//Read the headers first. Locate the ones you need
			int colNum = sheet.getRow(0).getLastCellNum();
	        XSSFRow rowHeader = sheet.getRow(0);
	        for (int j = 0; j < colNum; j++) {
	            XSSFCell cell = rowHeader.getCell(j);
	            headers.add(cell.getStringCellValue());
	            //String cellValue = getCellValue(cell);
	         
	        }

			List<DataRow> contents = new ArrayList<>();
			while (rows.hasNext()) {
				Row row = rows.next();
				if (!checkIfRowIsEmpty(row) && (row.getRowNum() != 0)) {

					DataRow dataRow = new DataRow();
					dataRow.setIndex(row.getRowNum());
					for (Cell cell : row) {
						// TODO safeguard for header resolving, cell column
						// index
						// might be out of bound of header array
						dataRow.put(headers.get(cell.getColumnIndex()),
								getCellValue(cell));
					}
					//System.out.println("dataRow====>" + dataRow);
					contents.add(dataRow);
				}
			}

			return new InputExcelFileVO(headers, contents);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("error while reading excel data"
					+ e.getMessage());
			throw new RuntimeException(e);

		}
	}

	private static String getCellValue(Cell cell) {
		if (cell == null) {
			return null;
		}
		if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			return cell.getStringCellValue();
		} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			return cell.getNumericCellValue() + "";
		} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			return cell.getBooleanCellValue() + "";
		} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			return cell.getStringCellValue();
		} else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
			return cell.getErrorCellValue() + "";
		} else {
			return null;
		}
	}

	private static boolean checkIfRowIsEmpty(Row row) {
		if (row == null) {
			return true;
		}
		if (row.getLastCellNum() <= 0) {
			return true;
		}
		for (int cellNum = row.getFirstCellNum(); cellNum < row
				.getLastCellNum(); cellNum++) {
			Cell cell = row.getCell(cellNum);
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK
					&& StringUtils.isNotBlank(cell.toString())) {
				return false;
			}
		}
		return true;
	}

}

