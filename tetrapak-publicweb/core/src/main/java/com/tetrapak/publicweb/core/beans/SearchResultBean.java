package com.tetrapak.publicweb.core.beans;

import java.util.Map;

public class SearchResultBean {
	
	private String title;
	
	private String description;
	
	private String productType;
	
	private Map<String, String> breadcrumbMap;
	
	private Map<String, String> tagsMap;
	
	private String path;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProductType() {
		return productType;
	}
	
	public void setProductType(String productType) {
		this.productType = productType;
	}

	public Map<String, String> getBreadcrumbMap() {
		return breadcrumbMap;
	}

	public void setBreadcrumbMap(Map<String, String> breadcrumbMap) {
		this.breadcrumbMap = breadcrumbMap;
	}

	public Map<String, String> getTagsMap() {
		return tagsMap;
	}

	public void setTagsMap(Map<String, String> tagsMap) {
		this.tagsMap = tagsMap;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
