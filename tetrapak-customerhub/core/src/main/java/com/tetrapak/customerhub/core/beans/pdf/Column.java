package com.tetrapak.customerhub.core.beans.pdf;

/**
 * This is column class and is used to create columns inside a table object
 *
 * @author Nitin Kumar
 */
public class Column {

    private String name;
    private float width;
    private boolean isBold;

    /**
     * Public constructor for this class
     *
     * @param name   string name
     * @param width  float width
     * @param isBold boolean isBold
     */
    public Column(String name, float width, boolean isBold) {
        this.name = name;
        this.width = width;
        this.isBold = isBold;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean bold) {
        isBold = bold;
    }
}
