package com.tetrapak.customerhub.core.beans;

/**
 * Header Bean class
 */
public class HeaderBean {

    private String href;

    private String name;

    private boolean targetNew;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTargetNew() {
        return targetNew;
    }

    public void setTargetNew(boolean targetNew) {
        this.targetNew = targetNew;
    }
}
