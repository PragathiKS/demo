package com.tetrapak.customerhub.core.beans.pdf;

import java.util.List;

/**
 * This is table builder class and is used to create table object
 *
 * @author Nitin Kumar
 */
public class TableBuilder {

    private Table table = new Table();

    public TableBuilder setNumberOfRows(Integer numberOfRows) {
        table.setNumberOfRows(numberOfRows);
        return this;
    }

    public TableBuilder setRowHeight(float rowHeight) {
        table.setRowHeight(rowHeight);
        return this;
    }

    public TableBuilder setContent(String[][] content) {
        table.setContent(content);
        return this;
    }

    public TableBuilder setColumns(List<Column> columns) {
        table.setColumns(columns);
        return this;
    }

    public TableBuilder setCellMargin(float cellMargin) {
        table.setCellMargin(cellMargin);
        return this;
    }

    public TableBuilder setMargin(float margin) {
        table.setMargin(margin);
        return this;
    }


    public TableBuilder setFontSize(float fontSize) {
        table.setFontSize(fontSize);
        return this;
    }


    /**
     * This method is used to build a table object
     *
     * @return table
     */
    public Table build() {
        return table;
    }
}
