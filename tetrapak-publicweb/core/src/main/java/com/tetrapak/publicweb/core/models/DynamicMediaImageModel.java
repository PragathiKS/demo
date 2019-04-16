package com.tetrapak.publicweb.core.models;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.tetrapak.publicweb.core.services.DynamicMediaService;


@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DynamicMediaImageModel {
    
    @SlingObject
    private SlingHttpServletRequest request;
    
    @Inject
    private String imagePath;
    
    @Inject
    private String mobileImagePath;
    
    @OSGiService
    private DynamicMediaService dynamicMediaService;

    private String mobilePortraitUrl;
    private String mobileLandscapeUrl;
    private String tabletPortraitUrl;
    private String tabletLandscapeUrl;
    private String desktopUrl;
    
    
    private static final String DESKTOP = "desktop";
    private static final String TABLETLANDSCAPE = "tabletL";
    private static final String TABLETPORTRAIT = "tabletP";
    private static final String MOBILELANDSCAPE = "mobileL";
    private static final String MOBILEPORTRAIT = "mobileP";
    private static final String FMT_PNG_ALPHA = "fmt=png-alpha";
    
    @PostConstruct
    protected void postConstruct() {
    	String dynamicMediaServiceUrl = getImageServiceURL();
        String rootPath = getRootPath();
        
        if (StringUtils.isNotEmpty(imagePath)) {
        	imagePath = StringUtils.substringAfterLast(StringUtils.substringBeforeLast(imagePath, "."), "/");
        }
        String dynamicMediaUrl = null;
        if (null != dynamicMediaServiceUrl) {
            dynamicMediaUrl = StringUtils.removeEndIgnoreCase(dynamicMediaServiceUrl, "/") + rootPath + "/" + imagePath;
        }
        
        if (StringUtils.isNotBlank(dynamicMediaUrl)) {
            desktopUrl = createDynamicMediaUrl(DESKTOP, dynamicMediaUrl);
            tabletPortraitUrl = createDynamicMediaUrl(TABLETPORTRAIT, dynamicMediaUrl);
            tabletLandscapeUrl = createDynamicMediaUrl(TABLETLANDSCAPE, dynamicMediaUrl);
            if (StringUtils.isNotBlank(mobileImagePath)) {
            	mobileImagePath = StringUtils.substringAfterLast(StringUtils.substringBeforeLast(mobileImagePath, "."), "/");
            	dynamicMediaUrl = StringUtils.removeEndIgnoreCase(dynamicMediaServiceUrl, "/") + rootPath + "/" + mobileImagePath;
            } 
            mobilePortraitUrl = createDynamicMediaUrl(MOBILEPORTRAIT, dynamicMediaUrl);
        	mobileLandscapeUrl = createDynamicMediaUrl(MOBILELANDSCAPE, dynamicMediaUrl);
        }
    }
    
    private String[] getDynamicMediaConfiguration() {
        return dynamicMediaService.getDynamicMediaConfMap();
    }
    
    private String getImageServiceURL() {
        return dynamicMediaService.getImageServiceUrl();
    }
    
    private String getRootPath() {
        return dynamicMediaService.getRootPath();
    }
    
    private Map<String, String> getMap(String[] dynamicMediaConfiguration) {
        Map<String, String> map = new HashMap<>();
        for (String propValue : dynamicMediaConfiguration) {
            map.put(StringUtils.substringBefore(propValue, "="), StringUtils.substringAfter(propValue, "="));
        }
        return map;
    }
    
    private String getComponentName(final Resource resource) {
        return StringUtils.substringAfterLast(resource.getResourceType(), "/");
    }
    
    private String createUrl(final String paramUrl, final String imageConfiguration) {
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
    
    private static boolean checkImageTransparency(final String paramUrl) {
        return StringUtils.contains(paramUrl, ".png") || StringUtils.contains(paramUrl, ".svg");
    }
    
    private static StringBuilder appendTransparency(final StringBuilder url, final String appendingString) {
        return url.append(appendingString).append(FMT_PNG_ALPHA);
    }
    
    private static String getUrl(final String paramUrl, final String width, final String height, final String crop) {
        boolean queryFlag = false;
        StringBuilder url = new StringBuilder(paramUrl);
        if (StringUtils.isNotEmpty(width)) {
            url.append("?wid=").append(width);
            queryFlag = true;
        }
        if (StringUtils.isNotEmpty(height)) {
            url = queryFlag ? url.append("&hei=").append(height)
                    : url.append("?hei=").append(height);
            queryFlag = true;
        }
        if (StringUtils.isNotEmpty(crop)) {
            url = queryFlag ? url.append("&cropn=").append(crop)
                    : url.append("?cropn=").append(crop);
            queryFlag = true;
        }
        
        if (checkImageTransparency(paramUrl)) {
            url = queryFlag ? appendTransparency(url, "&") : appendTransparency(url, "?");
        }
        
        return url.toString();
    }
    
    private String createDynamicMediaUrl(final String deviceType, final String imagePath) {
        
        String url = "";
        final Map<String, String> dynamicMediaConfiguration = getMap(getDynamicMediaConfiguration());
        final String componentName = getComponentName(request.getResource());
        if (StringUtils.isNotBlank(componentName)) {
            final StringBuilder key = new StringBuilder(componentName).append("-").append(deviceType);
            
            final String imageConfiguration = dynamicMediaConfiguration.get(key.toString());
            if (StringUtils.isNotEmpty(imageConfiguration)) {
                url = createUrl(imagePath, imageConfiguration);
            }
        }
        return url;
    }

	public String getMobilePortraitUrl() {
		return mobilePortraitUrl;
	}

	public String getMobileLandscapeUrl() {
		return mobileLandscapeUrl;
	}

	public String getTabletPortraitUrl() {
		return tabletPortraitUrl;
	}

	public String getTabletLandscapeUrl() {
		return tabletLandscapeUrl;
	}

	public String getDesktopUrl() {
		return desktopUrl;
	}
}

