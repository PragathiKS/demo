package com.tetrapak.customerhub.core.models;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.tetrapak.customerhub.core.models.DynamicImageModel;
import com.tetrapak.customerhub.core.services.ConfigurationService;



@Model(adaptables = {
        SlingHttpServletRequest.class
}, adapters = {
        DynamicImageModel.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = DynamicImageModelImpl.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")
public class DynamicImageModelImpl implements DynamicImageModel {
    
    /** The Constant RESOURCE_TYPE. */
    public static final String RESOURCE_TYPE = "/apps/customerhub/components/content/dynamicimage";
    
    /** The Constant FMT_PNG_ALPHA. */
    private static final String FMT_PNG_ALPHA = "fmt=png-alpha";
    
    /** The Constant HEIGHT. */
    private static final String HEIGHT = "hei";
    
    /** The Constant AMPERSAND. */
    private static final String AMPERSAND = "&";
    
    /** The Constant EQUALS. */
    private static final String EQUALS = "=";
    
    /** The Constant WIDTH. */
    private static final String WIDTH = "wid";
    
    /** The Constant QUERY_PARAMETER. */
    private static final String QUERY_PARAMETER = "?";
    
    /** The Constant HYPHEN. */
    private static final String HYPHEN = "-";
    
    /** The Constant CROPN. */
    private static final String CROPN = "cropn";
    
    /** The Constant DESKTOP. */
    private static final String DESKTOP = "desktop";
    
    /** The Constant DESKTOP. */
    private static final String DESKTOP_LARGE = "desktopL";
    
    /** The Constant TABLETLANDSCAPE. */
    private static final String TABLETLANDSCAPE = "tabletL";
    
    /** The Constant TABLETPORTRAIT. */
    private static final String TABLETPORTRAIT = "tabletP";
    
    /** The Constant MOBILELANDSCAPE. */
    private static final String MOBILELANDSCAPE = "mobileL";
    
    /** The Constant MOBILEPORTRAIT. */
    private static final String MOBILEPORTRAIT = "mobileP";

    /** The Constant PERSONALIZATION. */
	private static final CharSequence PERSONALIZATION = "-personalization";
    
    /**
     * Append transparency.
     *
     * @param url the url
     * @param appendingString the appending string
     * @return the string builder
     */
    private static StringBuilder appendTransparency(final StringBuilder url, final String appendingString) {
        return url.append(appendingString).append(FMT_PNG_ALPHA);
    }
    
    /**
     * Check image transparency.
     * @param paramUrl the param url
     * @return true, if successful
     */
    private static boolean checkImageTransparency(final String paramUrl) {
        return StringUtils.contains(paramUrl, ".png") || StringUtils.contains(paramUrl, ".svg");
    }
    
    /**
     * Creates the url.
     * @param paramUrl the url
     * @param imageConfiguration the image configuration
     * @return the string
     */
    private static String createUrl(final String paramUrl, final String imageConfiguration) {
        final int widthIndex = StringUtils.ordinalIndexOf(imageConfiguration, ",", 1);
        final int heightIndex = StringUtils.ordinalIndexOf(imageConfiguration, ",", 2);
        String width = null;
        String height = null;
        String crop = null;
        String url = paramUrl;
        if (widthIndex > -1) {
            width = imageConfiguration.substring(0, widthIndex);
            
            if (heightIndex > -1) {
                height = imageConfiguration.substring(widthIndex + 1, heightIndex);
                crop = imageConfiguration.substring(heightIndex + 1);
            } else {
                height = imageConfiguration.substring(widthIndex + 1);
            }
        } else {
            width = imageConfiguration;
        }
        if (StringUtils.isNotEmpty(width) || StringUtils.isNotEmpty(height) || StringUtils.isNotEmpty(crop)) {
            url = getUrl(paramUrl, width, height, crop);
        }
        return url;
    }
    
    /**
     * Gets the component name.
     * @param resource the resource
     * @return the component name
     */
    private static String getComponentName(final Resource resource) {
        return StringUtils.substringAfterLast(resource.getResourceType(), "/");
    }
    
    /**
     * Gets the url.
     *
     * @param paramUrl the param url
     * @param width the width
     * @param height the height
     * @param crop the crop
     * @return the url
     */
    private static String getUrl(final String paramUrl, final String width, final String height, final String crop) {
        boolean queryFlag = false;
        StringBuilder url = new StringBuilder(paramUrl);
        if (StringUtils.isNotEmpty(width)) {
            url.append(QUERY_PARAMETER).append(WIDTH).append(EQUALS).append(width);
            queryFlag = true;
        }
        if (StringUtils.isNotEmpty(height)) {
            url = queryFlag ? url.append(AMPERSAND).append(HEIGHT).append(EQUALS).append(height)
                    : url.append(QUERY_PARAMETER).append(HEIGHT).append(EQUALS).append(height);
            queryFlag = true;
        }
        if (StringUtils.isNotEmpty(crop)) {
            url = queryFlag ? url.append(AMPERSAND).append(CROPN).append(EQUALS).append(crop)
                    : url.append(QUERY_PARAMETER).append(CROPN).append(EQUALS).append(crop);
            queryFlag = true;
        }

        if (checkImageTransparency(paramUrl)) {
            url = queryFlag ? appendTransparency(url, AMPERSAND) : appendTransparency(url, QUERY_PARAMETER);
        }

        return url.toString();
    }
    
    /** The image path. */
    @Inject
    private String imagePath;
    
    /** The asset alt text. */
    @Inject
    private String assetAltText;
    
    /** The image view. */
    @Inject
    private String imageView;
    
    /** The sling request. */
    @Self
    private SlingHttpServletRequest slingRequest;
    
    /** The configuration service. */
    @OSGiService
    private ConfigurationService configurationService;
    
    /** The file reference. */
    @ValueMapValue
    private String fileReference;
    
    /** The view. */
    @ValueMapValue
    private String view;
    
    /** The view. */
    @ValueMapValue
    private String variationType;
    
    /** The alt text. */
    @ValueMapValue
    private String altText;

    /** The desktop url. */
    private String desktopUrl;
    
    /** The desktop large url. */
    private String desktopLargeUrl;

    /** The mobile url. */
    private String mobilePortraitUrl;

    /** The mobile landscape url. */
    private String mobileLandscapeUrl;

    /** The tablet url. */
    private String tabletPortraitUrl;
    
    /** The tablet landscape url. */
    private String tabletLandscapeUrl;
    
    /** The default image url. */
    private String defaultImageUrl;
    
    /** The has configuraton. */
    private boolean hasConfiguraton;
    
    /**
     * Creates the URL for the devices(Desktop,IPad,Mobile).
     * @param deviceType the device type
     * @return the string
     */
    private String createDynamicMediaUrl(final String deviceType, final String imagePath) {
        
        final Map<String, String> dynamicMediaConfiguration = getDynamicMediaConfiguration();
        String url = "";
        final String componentName = getComponentName(slingRequest.getResource());
        if (StringUtils.isNotBlank(componentName)) {
            final StringBuilder key = new StringBuilder(componentName).append(HYPHEN).append(deviceType);
            view = StringUtils.isNotBlank(imageView) ? imageView : view;
            if (StringUtils.isNotEmpty(view)) {
                key.append(HYPHEN).append(view);
            } else if (StringUtils.isNotEmpty(variationType)) {
                key.append(HYPHEN).append(variationType);
            }
            final String imageConfiguration = dynamicMediaConfiguration.get(key.toString());
            if (StringUtils.isNotEmpty(imageConfiguration)) {
                url = createUrl(imagePath, imageConfiguration);
                hasConfiguraton = true;
            }
        }
        return url;
    }
    
    
    @Override
    public String getAltText() {
        return StringUtils.isNotEmpty(altText) ? altText : assetAltText;
    }
    
   
    @Override
    public String getDefaultImageUrl() {
        return defaultImageUrl;
    }

   
    @Override
    public String getDesktopUrl() {
        return desktopUrl;
    }
    
   
    @Override
    public Map<String, String> getDynamicMediaConfiguration() {
        return configurationService.getDynamicMediaConfMap();
    }
    
   
    @Override
    public String getFileReference() {
        return fileReference;
    }
    
    
    @Override
    public String getImageServiceURL() {
        return configurationService.getImageServiceUrl();
    }
    
   
    @Override
    public String getMobileLandscapeUrl() {
        return mobileLandscapeUrl;
    }
    
   
    @Override
    public String getMobilePortraitUrl() {
        return mobilePortraitUrl;
    }
    
    
    @Override
    public String getTabletLandscapeUrl() {
        return tabletLandscapeUrl;
    }
    
    
    @Override
    public String getTabletPortraitUrl() {
        return tabletPortraitUrl;
    }
    
    
    @Override
    public String getVariationType() {
        return variationType;
    }
    
    
    @Override
    public String getView() {
        return view;
    }
    
    
    @PostConstruct
    protected void postConstruct() {
        String dynamicMediaUrl = getImageServiceURL();
		if (StringUtils.isNotBlank(assetAltText) && assetAltText.contains(PERSONALIZATION)) {
			dynamicMediaUrl = StringUtils.isNotBlank(dynamicMediaUrl)
					? StringUtils.removeEndIgnoreCase(dynamicMediaUrl, "/") + imagePath
					: imagePath;
		} else {
			fileReference = StringUtils.isNotBlank(fileReference) ? fileReference : imagePath;
			if (fileReference == null) {
				dynamicMediaUrl = StringUtils.EMPTY;
			}
			fileReference = StringUtils.isNotBlank(dynamicMediaUrl)
					? StringUtils.removeEndIgnoreCase(dynamicMediaUrl, "/") + fileReference
					: fileReference;
		}
        if (StringUtils.isNotBlank(assetAltText) && assetAltText.contains(PERSONALIZATION)) {
            setDesktopUrl(createDynamicMediaUrl(DESKTOP, dynamicMediaUrl));
            setDesktopLargeUrl(createDynamicMediaUrl(DESKTOP_LARGE, dynamicMediaUrl));
            setMobilePortraitUrl(createDynamicMediaUrl(MOBILEPORTRAIT, dynamicMediaUrl));
            setMobileLandscapeUrl(createDynamicMediaUrl(MOBILELANDSCAPE, dynamicMediaUrl));
            setTabletPortraitUrl(createDynamicMediaUrl(TABLETPORTRAIT, dynamicMediaUrl));
            setTabletLandscapeUrl(createDynamicMediaUrl(TABLETLANDSCAPE, dynamicMediaUrl));
        } else if (StringUtils.isNotBlank(dynamicMediaUrl)) {
        	setDesktopUrl(createDynamicMediaUrl(DESKTOP, fileReference));
            setDesktopLargeUrl(createDynamicMediaUrl(DESKTOP_LARGE, fileReference));
            setMobilePortraitUrl(createDynamicMediaUrl(MOBILEPORTRAIT, fileReference));
            setMobileLandscapeUrl(createDynamicMediaUrl(MOBILELANDSCAPE, fileReference));
            setTabletPortraitUrl(createDynamicMediaUrl(TABLETPORTRAIT, fileReference));
            setTabletLandscapeUrl(createDynamicMediaUrl(TABLETLANDSCAPE, fileReference));
        }
        setDefaultImage();
    }
    
    /**
     * Sets the default image.
     */
    public void setDefaultImage() {
        if (hasConfiguraton) {
            setDefaultImageUrl("/etc/designs/roche/pharma/customerportal/clientlibs/global.publish/images/blank.png");
        } else if (null != fileReference) {
            StringBuilder defalturl = new StringBuilder(fileReference).append(QUERY_PARAMETER).append("scl=1");
            
            if (checkImageTransparency(fileReference)) {
                defalturl = appendTransparency(defalturl, AMPERSAND);
            }
            
            setDefaultImageUrl(defalturl.toString());
        }
    }
    
    
    @Override
    public void setDefaultImageUrl(final String defaultImageUrl) {
        this.defaultImageUrl = defaultImageUrl;
    }
    
    
    public void setDesktopUrl(final String desktopUrl) {
        this.desktopUrl = desktopUrl;
    }
    
   
    public String getDesktopLargeUrl() {
        return desktopLargeUrl;
    }

    public void setDesktopLargeUrl(String desktopLargeUrl) {
        this.desktopLargeUrl = desktopLargeUrl;
    }
    
    
    public void setMobileLandscapeUrl(final String mobileLandscapeUrl) {
        this.mobileLandscapeUrl = mobileLandscapeUrl;
    }
    
   
    public void setMobilePortraitUrl(final String mobilePortraitUrl) {
        this.mobilePortraitUrl = mobilePortraitUrl;
    }

    
    public void setTabletLandscapeUrl(final String tabletLandscapeUrl) {
        this.tabletLandscapeUrl = tabletLandscapeUrl;
    }

    
    public void setTabletPortraitUrl(final String tabletPortraitUrl) {
        this.tabletPortraitUrl = tabletPortraitUrl;
    }
    
}
