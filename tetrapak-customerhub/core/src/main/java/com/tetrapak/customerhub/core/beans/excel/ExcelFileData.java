package com.tetrapak.customerhub.core.beans.excel;

import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * Bean class for excel file data
 * 
 * @author Tushar
 * @author swalamba
 *
 */
public class ExcelFileData {
	private String excelSheetName;
	private short cellBorderColor = IndexedColors.BLACK.index;
	private String fileName;
	private String[][] data;
	private boolean hasMargin = true;

	public String getExcelSheetName() {
		return excelSheetName;
	}

	public void setExcelSheetName(String excelSheetName) {
		this.excelSheetName = excelSheetName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String[][] getData() {
		return data;
	}

	public void setData(String[][] data) {
		this.data = data;
	}

	public short getCellBorderColor() {
		return cellBorderColor;
	}

	public void setCellBorderColor(short cellBorderColor) {
		this.cellBorderColor = cellBorderColor;
	}

	public boolean isHasMargin() {
		return hasMargin;
	}

	public void setHasMargin(boolean hasMargin) {
		this.hasMargin = hasMargin;
	}

}
