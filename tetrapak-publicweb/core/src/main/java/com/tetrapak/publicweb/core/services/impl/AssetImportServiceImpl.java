package com.tetrapak.publicweb.core.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.publicweb.core.beans.pxp.AssetDetail;
import com.tetrapak.publicweb.core.services.AssetImportService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;

/**
 * IMPL class for Asset Import Service
 * 
 * @author akabansa1
 *
 */
@Component(immediate = true, service = AssetImportService.class, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class AssetImportServiceImpl implements AssetImportService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AssetImportServiceImpl.class);

	@Override
	public AssetDetail getAssetDetailfromInputStream(String sourceurl) {

		AssetDetail assetDetail = new AssetDetail();
		try {
			URL url = new URL(sourceurl);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			int responseCode = httpConn.getResponseCode();

			// check HTTP response code first
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream is = httpConn.getInputStream();
				assetDetail.setFileName(GlobalUtil.getFileName(sourceurl));
				assetDetail.setContentType(httpConn.getContentType());
				assetDetail.setIs(is);
			} else {
				LOGGER.error("Error occured while fetching assets from PXP {}", responseCode);
			}
		} catch (IOException e) {
			LOGGER.error("Error occured while fetching assets from PXP {}", e);
		}
		return assetDetail;
	}	

}
