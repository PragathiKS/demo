package com.tetrapak.publicweb.core.services;

import com.tetrapak.publicweb.core.beans.pxp.AssetDetail;

/**
 * 
 * @author Akash Bansal
 *
 */
public interface AssetImportService {

	/**
	 * Function to fetch assets binary stream from given URL
	 * @param url
	 * @return
	 */
	AssetDetail getAssetDetailfromInputStream(String url);	
	
}

