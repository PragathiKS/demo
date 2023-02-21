package com.tetralaval.beans;

public class Dropdown {

    private String label;
    private String value;

    public Dropdown(String label, String value) {
	super();
	this.label = label;
	this.value = value;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    public String getLabel() {
	return label;
    }

    public void setLabel(String label) {
	this.label = label;
    }

}
