package com.trs.core.reports;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class StatefulReport {

    private XSSFWorkbook reportWorkbook;
    private XSSFSheet reportWorkBooksheet;
    private Integer rowCounter;

    public StatefulReport(Builder builder) {
        this.rowCounter = builder.rowCounter;
        this.reportWorkbook = builder.reportWorkbook;
        this.reportWorkBooksheet = builder.reportWorkBooksheet;
    }

    public XSSFWorkbook getReportWorkbook() {
        return reportWorkbook;
    }

    public void setReportWorkbook(XSSFWorkbook reportWorkbook) {
        this.reportWorkbook = reportWorkbook;
    }

    public XSSFSheet getReportWorkBooksheet() {
        return reportWorkBooksheet;
    }

    public void setReportWorkBooksheet(XSSFSheet reportWorkBooksheet) {
        this.reportWorkBooksheet = reportWorkBooksheet;
    }

    public Integer getRowCounter() {
        return rowCounter;
    }

    public void setRowCounter(Integer rowCounter) {
        this.rowCounter = rowCounter;
    }

    public StatefulReport createExcelSheetRow(String[] columnValues) {

        if (columnValues != null) {
            int columnCounter = 0;
            Row row = this.getReportWorkBooksheet().createRow(this.getRowCounter());

            for (String columnValue : columnValues) {
                Cell column = row.createCell(columnCounter);
                column.setCellValue(columnValue);
                columnCounter++;
            }
            this.rowCounter++;
        }
        return this;
    }

    public static class Builder {

        private XSSFWorkbook reportWorkbook;
        private XSSFSheet reportWorkBooksheet;
        private Integer rowCounter;

        public Builder() {
            this.rowCounter = 0;
        }

        public Builder reportWorkbook() {

            this.reportWorkbook = new XSSFWorkbook();
            return this;

        }

        public Builder reportWorkBooksheet() {

            this.reportWorkBooksheet = this.reportWorkbook.createSheet("log");
            return this;
        }

        public Builder reportWorkBooksheet(String name) {

            this.reportWorkBooksheet = this.reportWorkbook.createSheet(name);
            return this;

        }

        public StatefulReport build() {
            return new StatefulReport(this);
        }
    }

}
