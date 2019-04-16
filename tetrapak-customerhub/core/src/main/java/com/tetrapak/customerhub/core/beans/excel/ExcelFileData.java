package com.tetrapak.customerhub.core.beans.excel;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean class for excel file data
 *
 * @author Tushar
 */
public class ExcelFileData {
    private static String excelSheetName;
    private short fontHeight;
    private short fontColor;
    private String fileName;
    private List<String> columns = new ArrayList<>();
    private String[][] data;

    public String getExcelSheetName() {
        return excelSheetName;
    }

    public void setExcelSheetName(String excelSheetName) {
        ExcelFileData.excelSheetName = excelSheetName;
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
        return new ArrayList<>(columns);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setColumns(List<String> columns) {
        this.columns = new ArrayList<>(columns);
    }

    public String[][] getData() {
        return data.clone();
    }

    public void setData(String[][] data) {
        this.data = data.clone();
    }
}
