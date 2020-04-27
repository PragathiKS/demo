package com.tetrapak.publicweb.core.services;

import com.tetrapak.publicweb.core.beans.pxp.AssetDetail;

/**
 * 
 */
public interface AssetImportService {

	AssetDetail getAssetDetailfromInputStream(String url);
	
	String getDAMRootPath();
	
	String[] getAssetTypeMapping();
	
	
}

