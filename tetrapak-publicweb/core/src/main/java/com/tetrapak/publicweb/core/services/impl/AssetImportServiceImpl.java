package com.tetrapak.publicweb.core.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
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
		String[] contentTypeMapping() default "[images=image/png\\,image/jpeg]";

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
				String contentType = httpConn.getContentType();
				InputStream is = httpConn.getInputStream();
				assetDetail.setFileName(GlobalUtil.validatingAssetName(getFileName(sourceurl)));
				assetDetail.setContentType(contentType);
				assetDetail.setIs(is);
				
			}else {
				LOGGER.error("Error occured while fetching assets from PXP {}", responseCode);
			}
		} catch (IOException e) {
			LOGGER.error("Error occured while fetching assets from PXP {}", e);
		}
		return assetDetail;
	}
	
	
	@Override
	public String getDAMPath(String productId, String categoryId, String sourceurl) {
		return	getDAMPath(productId, categoryId, getAssetDetailfromInputStream(sourceurl));
	}

	@Override
	public String getDAMPath(String productId, String categoryId, AssetDetail assetDetail) {

		String assetType = getassetType(assetDetail.getContentType());
		if(!StringUtils.isEmpty(assetType) && !StringUtils.isEmpty(categoryId) && !StringUtils.isEmpty(productId) && !StringUtils.isEmpty(assetDetail.getFileName())) {
	        return new StringBuffer(this.config.damRootPath()).append("/").append(categoryId).append("/").append(productId).append("/").append(assetType).append("/").append(assetDetail.getFileName()).toString();	
		}else {
			LOGGER.error("One of the mandatory input not provided for DAM path \n Asset Type {}  \nCategoryId  {} \nProductId {} \nfileName {}",assetType,categoryId, productId,assetDetail.getFileName());
			return "";
		}
		
	}


	/**
	 * 
	 * @param fileURL
	 * @return
	 */
	private String getFileName(String fileURL) {
		// extracts file name from URL
		return fileURL.substring(fileURL.lastIndexOf('/') + 1, fileURL.length());
	}

	/**
	 * 
	 * @param contentType
	 * @return
	 */
	private String getassetType(String contentType) {
		String assetType = "";
		String[] contentTypeMapping = this.config.contentTypeMapping();
		for (String mapping : contentTypeMapping) {
			if(mapping.contains(contentType)) {
				assetType = mapping.split("=")[0];
				break;
			}
		}
		LOGGER.debug("asset Type {}",assetType);
		return assetType;
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

	
}
