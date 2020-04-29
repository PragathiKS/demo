package com.tetrapak.publicweb.core.beans.pxp;

import java.io.InputStream;

/**
 * 
 * @author Akash Bansal
 *
 */
public class AssetDetail {
	
	private InputStream is;
	private String contentType;
	private String fileName;
	/**
	 * @return the is
	 */
	public InputStream getIs() {
		return is;
	}
	/**
	 * @param is the is to set
	 */
	public void setIs(final InputStream is) {
		this.is = is;
	}
	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}
	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(final String contentType) {
		this.contentType = contentType;
	}
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	
	
	
}
