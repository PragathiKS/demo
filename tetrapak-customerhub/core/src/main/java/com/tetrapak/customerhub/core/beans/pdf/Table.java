package com.tetrapak.customerhub.core.beans.pdf;

import java.util.ArrayList;
import java.util.List;

/**
 * This is table class and is used to create table objects
 * which is used to draw content to a pdf document in a tabular format
 *
 * @author Nitin Kumar
 */
public class Table {

    // Table attributes
    private float margin;
    private float rowHeight;

    // font attributes
    private float fontSize;

    // Content attributes
    private Integer numberOfRows;
    private List<Column> columns;
    private String[][] content;
    private float cellMargin;

    public Table() {
        //no implementation required
    }

    public Integer getNumberOfColumns() {
        return this.getColumns().size();
    }

    public float getWidth() {
        float tableWidth = 0f;
        for (Column column : columns) {
            tableWidth += column.getWidth();
        }
        return tableWidth;
    }

    public float getMargin() {
        return margin;
    }

    public void setMargin(float margin) {
        this.margin = margin;
    }


    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public String[] getColumnsNamesAsArray() {
        String[] columnNames = new String[getNumberOfColumns() + 1];
        for (int i = 0; i < getNumberOfColumns(); i++) {
            columnNames[i] = columns.get(i).getName();
        }
        return columnNames;
    }

    public List<Column> getColumns() {
        return new ArrayList<>(columns);
    }

    public void setColumns(List<Column> columns) {
        this.columns = new ArrayList<>(columns);
    }

    public Integer getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(Integer numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public float getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(float rowHeight) {
        this.rowHeight = rowHeight;
    }

    public String[][] getContent() {
        return content.clone();
    }

    public void setContent(String[][] content) {
        this.content = content.clone();
    }

    public float getCellMargin() {
        return cellMargin;
    }

    public void setCellMargin(float cellMargin) {
        this.cellMargin = cellMargin;
    }
}
