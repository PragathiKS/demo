package com.tetrapak.customerhub.core.beans.excel;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean class for excel file data
 * @author Tushar
 *
 */
public class ExcelFileData {
	private String excelSheetName;
	private String font;
	private short fontHeight;
	private short fontColor;
	private String fileName;
	private List<String> columns = new ArrayList<>();
	private String[][] data;
   
    public String getExcelSheetName() {
        return excelSheetName;
    }
    public void setExcelSheetName(String excelSheetName) {
        this.excelSheetName = excelSheetName;
    }
    public String getFont() {
    	return font;
    }
    public void setFont(String font) {
    	this.font = font;
    }   
    public short getFontHeight() {
        return fontHeight;
    }
    public void setFontHeight(short fontHeight) {
        this.fontHeight = fontHeight;
    }
    public short getFontColor() {
        return fontColor;
    }
    public void setFontColor(short fontColor) {
        this.fontColor = fontColor;
    }
    public List<String> getColumns() {
        return columns;
    }
    
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public void setColumns(List<String> columns) {
        this.columns = columns;
    }
    public String[][] getData() {
        return data;
    }
    public void setData(String[][] data) {
        this.data = data;
    }        
}

