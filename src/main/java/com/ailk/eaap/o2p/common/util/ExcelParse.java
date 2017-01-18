package com.ailk.eaap.o2p.common.util;



import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.asiainfo.foundation.log.Logger;

public class ExcelParse {
	
	private final static Logger logger = Logger.getLog(ExcelParse.class);

	public static Map<String,Map<String,List>>  readExcel(File file){
        if(file==null){
        	return null;
        }
		boolean isE2007 = false;	
		if(file.getName().endsWith("xlsx")){
			isE2007 = true;
		}
		InputStream input = null;	
		try {
			input = new FileInputStream(file);	
			Workbook wb  = null;
			if(isE2007){
				wb = new XSSFWorkbook(input);
			}else{
				wb = new HSSFWorkbook(input);
			}
			Map<String,Map<String,List>> sheetMap = new HashMap<String,Map<String,List>>();
			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				Sheet sheet = wb.getSheetAt(i);
				if(sheet==null){
					continue;
				}
				String sheetName = sheet.getSheetName();
				Iterator<Row> rows = sheet.rowIterator();
				
				
				Map<String,List> map = new HashMap<String,List>();
				while (rows.hasNext()) {
					Row row = rows.next();
					List valuelist = new ArrayList(); 

					for (int j = 0; j < row.getLastCellNum(); j++) {
		
						Cell cell = row.getCell(j);
						if(null==cell){
							valuelist.add("");
							continue;
						}
						switch (cell.getCellType()) {	
						case HSSFCell.CELL_TYPE_NUMERIC:
							try {
								valuelist.add(new BigDecimal(cell.getNumericCellValue()+"").toPlainString());
							} catch (Exception e) {
								// TODO: handle exception
								valuelist.add("");
							}
							
							break;
						case HSSFCell.CELL_TYPE_STRING:
						
							try {
								valuelist.add(cell.getStringCellValue()+"");
							} catch (Exception e) {
								// TODO: handle exception
								valuelist.add("");

							}
							break;
						case HSSFCell.CELL_TYPE_BOOLEAN:
							try {
								valuelist.add(cell.getBooleanCellValue()+"");
							} catch (Exception e) {
								// TODO: handle exception
								valuelist.add("");

							}
							
							break;
						case HSSFCell.CELL_TYPE_FORMULA:
							
							try {
								valuelist.add(cell.getCellFormula()+"");
							} catch (Exception e) {
								// TODO: handle exception
								valuelist.add("");
							}
							break;
						default:
							try {
								valuelist.add(cell.getStringCellValue()+"");
							} catch (Exception e) {
								// TODO: handle exception
								valuelist.add("");
							}
						break;
						}
					}
					map.put(row.getRowNum()+"", valuelist);
				}
				sheetMap.put(sheetName, map);
			}
			return sheetMap;
			
		} catch (Exception e) {
			logger.error("parse excel  error,the fileName is {0},exception is {1}",new String[]{file.getPath(),e.toString()});
			return null;
		}finally{
			
			try{
				if(input!=null){
					input.close();
				}
			}catch(Exception ex){
				logger.error(ex.getMessage(), ex);
			}
		}
		
		
	}
}
