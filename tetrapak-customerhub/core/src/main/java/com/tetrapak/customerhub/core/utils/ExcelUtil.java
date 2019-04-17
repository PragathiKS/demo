package com.tetrapak.customerhub.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.SlingHttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.customerhub.core.beans.excel.ExcelFileData;
import com.tetrapak.customerhub.core.exceptions.ExcelReportRuntimeException;

/**
 * @author dhitiwar
 *
 */
public class ExcelUtil {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);
	private ExcelUtil() {
	    //Disallowed to create Object out of class
	}
	/**
     * 
     * @return Workbook
     * 
     */
	private static Workbook getExcelWorkBook(){
	   	    return new XSSFWorkbook();
     }
	
	/**
	 * 
	 * @param workBook
	 * @param sheetName
	 * @return Sheet
	 * 
	 */
	private static Sheet getExcelSheet(Workbook workBook, String sheetName){
		Sheet sheet = null;
		if(Objects.nonNull(workBook) && Objects.nonNull(sheetName)) {
			sheet = workBook.createSheet(sheetName);
        } 
		return sheet;
	}
	
	/**
	 * @param workBook
	 * @param excelReportData
	 * @return
	 */
	private static Font getFontStylingForHeader(Workbook workBook,  ExcelFileData excelReportData) {
	    Font headerFont = null;
	    RichTextString rt = new HSSFRichTextString();
		 if(Objects.nonNull(workBook) && Objects.nonNull(excelReportData)) {
		     headerFont = workBook.createFont();
	         headerFont.setBold(true);
	         headerFont.setFontHeightInPoints(excelReportData.getFontHeight());
	         headerFont.setColor(excelReportData.getFontColor());
		 }
	     return headerFont;
	}
	/**
     * @param workBook
     * @param headerFont
     * @return CellStyle
     */
	private static CellStyle getHeaderCellStyle(Workbook workBook, Font headerFont) {
	     CellStyle headerCellStyle = null;
		 if(Objects.nonNull(workBook) && Objects.nonNull(headerFont)) {
		     headerCellStyle = workBook.createCellStyle();
	         headerCellStyle.setFont(headerFont);
		 }
	     return headerCellStyle;
	}
	// Create a header Row
	
	/**
	 * @param sheet
	 * @param headerCellStyle
	 * @param headerColumnLabels
	 * @return Row
	 */ 
	private static Row getHeaderLabels(Sheet sheet, CellStyle headerCellStyle , List<String> headerColumnLabels){
	    Row headerRow  = null;
		  if(Objects.nonNull(sheet) && Objects.nonNull(headerCellStyle) && Objects.nonNull(headerColumnLabels)) {
		       headerRow = getRow(sheet, 0);
		        // Create cells
		       if(Objects.nonNull(headerRow)) {
		           for(int i = 0; i < headerColumnLabels.size(); i++) {
	                    Cell cell = headerRow.createCell(i);
	                    cell.setCellValue(headerColumnLabels.get(i));
	                    cell.setCellStyle(headerCellStyle);
	                } 
		       }
		 }
		 
        return headerRow;
	}
	 /**
	 * @param sheet
	 * @param rowNum
	 * @return Row
	 */
	private static Row getRow(Sheet sheet, int rowNum) {
   	  Row row = null;
   	  if(Objects.nonNull(sheet)) {
   	      row = sheet.createRow(++rowNum);
   	  }
   	  return row;
	 }
	
	 /**
	 * @param sheet
	 * @param cloumnCount
	 */
	private static void resizeCellToFitContent(Sheet sheet, int cloumnCount) {
		// Resize all columns to fit the content size
	        for(int i = 0; i < cloumnCount; i++) {
	            sheet.autoSizeColumn(i);
	        }
	 }
	
	 /**
	 * @param workBook
	 * @throws IOException
	 */
	private static void closeWorkbook(Workbook workBook) throws IOException{
		 if(Objects.nonNull(workBook)) {
		     workBook.close();
		 }
	 }
	 /**
     * @param response
     * @param excelReportData
     * 
     */
	 public static void generateExcelReport(SlingHttpServletResponse response, ExcelFileData excelReportData){
	    try {
            Workbook workBook = getExcelWorkBook();
            Sheet sheet = getExcelSheet(workBook, excelReportData.getExcelSheetName());
            Font headerFont = getFontStylingForHeader(workBook, excelReportData);
            CellStyle headerCellStyle = getHeaderCellStyle(workBook, headerFont);
            Row headerRow = getHeaderLabels(sheet, headerCellStyle, excelReportData.getColumns());
            prepareReportData(sheet, headerRow, excelReportData.getData(), excelReportData);
            resizeCellToFitContent(sheet, excelReportData.getColumns().size());
            downloadExcel(response, workBook, excelReportData);
        } catch (Exception e) {
            LOGGER.error("\nA run-time exception occured while generating excel report.");
            throw new ExcelReportRuntimeException("Something went wrong while generateExcelReport in ExcelUtil class", e);
        }
	 }
	 /**
	     * @param sheet
	     * @param row
	     * @param String[][] reportData
	     * 
	     */
	 private static void prepareReportData(Sheet sheet, Row row, String[][] reportData, ExcelFileData ed ) {
	     int rowCount = row.getRowNum();
	     
	     for (String[] data : reportData) {
	            row  = getRow(sheet, rowCount);
	            int columnCount = 0;
	            if(Objects.nonNull(row)) {
	                for (String field : data) {
	                    Cell cell = row.createCell(columnCount++);
	                    XSSFRichTextString richText = new XSSFRichTextString(field);
	                    Font f = getFontStylingForHeader(getExcelWorkBook(), ed);
	                    f.setBold(true);
	                    richText.applyFont(f);
	                    cell.setCellValue(richText);	                }   
	            }
	          ++rowCount;  
	        }
	 }
	 /**
      * @param response
      * @param workBook
      * @param ExcelFileData excelReportData
      * 
      */
	 private static void downloadExcel(SlingHttpServletResponse response, Workbook workBook, ExcelFileData excelReportData) throws IOException{
        
            ByteArrayOutputStream out = new ByteArrayOutputStream();
             workBook.write(out);
             byte[] docBytes = out.toByteArray();
             ByteArrayInputStream in = new ByteArrayInputStream(docBytes);
             response.setContentType("application/x-ms-excel");
             response.setHeader("Content-Disposition", "attachment; filename=" + excelReportData.getFileName()+".xlsx");
             int read;
             OutputStream os = response.getOutputStream();
             while ((read = in.read(docBytes)) != -1) {
                 os.write(docBytes, 0, read);
             }
             os.flush();
             os.close();
             closeWorkbook(workBook);
        
     } 
	 
}