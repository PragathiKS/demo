package com.tetrapak.customerhub.core.pdf;

/**
 * This is column class and is used to create columns inside a table object
 *
 * @author Nitin Kumar
 */
public class Column {

    private String name;
    private float width;

    /**Public constructor for this class
     * @param name  string name
     * @param width float width
     */
    public Column(String name, float width) {
        this.name = name;
        this.width = width;
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
}
