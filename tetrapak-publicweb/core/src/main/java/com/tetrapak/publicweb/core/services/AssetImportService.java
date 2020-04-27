package com.tetrapak.publicweb.core.services;

import com.tetrapak.publicweb.core.beans.pxp.AssetDetail;

/**
 * 
 */
public interface AssetImportService {

	AssetDetail getAssetDetailfromInputStream(String url);

	String getDAMPath(String productId, String categoryId, String sourceurl);

	String getDAMPath(String productId, String categoryId, AssetDetail assetdetail);
}

