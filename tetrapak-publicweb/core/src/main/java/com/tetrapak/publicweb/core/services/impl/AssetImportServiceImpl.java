package com.tetrapak.publicweb.core.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
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
@Designate(ocd = AssetImportServiceImpl.Config.class)
public class AssetImportServiceImpl implements AssetImportService {

	@ObjectClassDefinition(name = "Full Feed Product Assets Import Sling Job", description = "Asset Import Sling job")
	public static @interface Config {

		@AttributeDefinition(name = "DAM root Path")
		String damRootPath() default "/content/dam/tetrapak/publicweb/pxp";

		@AttributeDefinition(name = "Content Type Mapping", description = "mapping of contenttype to a asset type")
		String[] contentTypeMapping() default "[images=jpg\\,png,videos=mp4]";

	}

	private Config config;

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

	/**
	 * activate method
	 *
	 * @param config site Improve Script URL configuration
	 */
	@Activate
	public void activate(Config config) {
		this.config = config;
	}

	/**
	 * @return the config
	 */
	public Config getConfig() {
		return config;
	}

	@Override
	public String getDAMRootPath() {
		return this.config.damRootPath();
	}

	@Override
	public String[] getAssetTypeMapping() {
		return this.config.contentTypeMapping();
	}

}
