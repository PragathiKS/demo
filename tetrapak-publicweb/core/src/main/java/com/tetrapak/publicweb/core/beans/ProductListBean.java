package com.tetrapak.publicweb.core.beans;

public class ProductListBean {
	
	private String tabLinkTextI18n;
	
	private String categoryTag;
	
	private String categoryTagAnalyticsPath;

	public String getTabLinkTextI18n() {
		return tabLinkTextI18n;
	}

	public void setTabLinkTextI18n(String tabLinkTextI18n) {
		this.tabLinkTextI18n = tabLinkTextI18n;
	}

	public String getCategoryTag() {
		return categoryTag;
	}

	public void setCategoryTag(String categoryTag) {
		this.categoryTag = categoryTag;
	}
	
	public String getCategoryTagAnalyticsPath() {
		return categoryTagAnalyticsPath;
	}

	public void setCategoryTagAnalyticsPath(String categoryTagAnalyticsPath) {
		this.categoryTagAnalyticsPath = categoryTagAnalyticsPath;
	}

}
