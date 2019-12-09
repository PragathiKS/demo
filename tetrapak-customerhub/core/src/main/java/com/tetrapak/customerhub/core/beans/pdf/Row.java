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
    private boolean isHref;
    private String link;


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


    public boolean isHref() {
        return isHref;
    }

    public void setHref(boolean href) {
        isHref = href;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
