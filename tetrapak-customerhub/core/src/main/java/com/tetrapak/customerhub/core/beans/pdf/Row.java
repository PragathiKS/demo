package com.tetrapak.customerhub.core.beans.pdf;

/**
 * This is column class and is used to create columns inside a table object
 *
 * @author Nitin Kumar
 */
public class Row {

    private String content;
    private int fontSize;
    private int height;

    /**
     * Public constructor for this class
     *
     * @param content  string content
     * @param fontSize font Size
     */
    public Row(String content, int height, int fontSize) {
        this.content = content;
        this.fontSize = fontSize;
        this.height = height;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
