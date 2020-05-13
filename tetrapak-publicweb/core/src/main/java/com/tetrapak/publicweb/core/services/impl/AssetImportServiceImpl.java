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
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.AssetImportService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;

/**
 * IMPL class for Asset Import Service
 *
 * @author Akash Bansal
 *
 */
@Component(immediate = true, service = AssetImportService.class, configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Designate(ocd = AssetImportServiceImpl.AssetImportServiceConfig.class)
public class AssetImportServiceImpl implements AssetImportService {

    /**
     * The Interface AssetImportServiceConfig.
     */
    @ObjectClassDefinition(
            name = "PXP Asset Import Service Configuration",
            description = "PXP Asset Import Service Configuration")
    @interface AssetImportServiceConfig {

        @AttributeDefinition(
                name = "Content Type Mapping",
                description = "Content Type Mapping, add comma seperated mapping in fomrmat video/mp4=mp4")
        String[] getContentTypeMapping() default "/content/dam/tetrapak/findMyOffice/contentFragments/countries";

    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AssetImportServiceImpl.class);
    private AssetImportServiceConfig config;

    @Override
    public AssetDetail getAssetDetailfromInputStream(final String sourceurl) {

        AssetDetail assetDetail = null;
        try {
            final URL url = new URL(sourceurl);
            final HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            final int responseCode = httpConn.getResponseCode();

            // check HTTP response code first
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String contentType = httpConn.getContentType();
                LOGGER.debug("Success :: Fetching assets from PXP URL: {} Content Type {}", sourceurl, contentType);

                // update content type with filename extension in case not supported content type is provided
                if (PWConstants.APPLICATION_OCTET_STREAM.equalsIgnoreCase(contentType)) {
                    contentType = GlobalUtil.getAssetContentType(sourceurl, config.getContentTypeMapping());
                }
                assetDetail = new AssetDetail();
                final InputStream is = httpConn.getInputStream();
                assetDetail.setFileName(GlobalUtil.getFileName(sourceurl));
                assetDetail.setContentType(contentType);
                assetDetail.setIs(is);
            } else {
                LOGGER.error("Error occured while fetching assets from PXP {} Error code {}", sourceurl, responseCode);
            }
        } catch (final IOException e) {
            LOGGER.error("Error occured while fetching assets from PXP {}  Error :: {}", sourceurl, e.getMessage(), e);
        }
        return assetDetail;
    }

    /**
     * activate method.
     *
     * @param config
     *            site Improve Script URL configuration
     */
    @Activate
    public void activate(final AssetImportServiceConfig config) {
        this.config = config;
    }

}
