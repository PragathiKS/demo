package com.tetrapak.publicweb.core.beans;

public class FooterBean {

	private String linkTextI18n;
	
	private String linkTooltipI18n;
	
	private String linkPath; 

    private String targetBlank;

	public String getLinkTextI18n() {
		return linkTextI18n;
	}

	public void setLinkTextI18n(String linkTextI18n) {
		this.linkTextI18n = linkTextI18n;
	}

	public String getLinkTooltipI18n() {
		return linkTooltipI18n;
	}

	public void setLinkTooltipI18n(String linkTooltipI18n) {
		this.linkTooltipI18n = linkTooltipI18n;
	}

	public String getLinkPath() {
		return linkPath;
	}

	public void setLinkPath(String linkPath) {
		this.linkPath = linkPath;
	}

	public String getTargetBlank() {
		return targetBlank;
	}

	public void setTargetBlank(String targetBlank) {
		this.targetBlank = targetBlank;
	}

}
