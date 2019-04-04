package com.tetrapak.customerhub.core.models;

import java.util.Map;

public interface DynamicImageModel {
    
    public String getFileReference();
    
    public String getImageServiceURL();
    
    public String getMobileLandscapeUrl();
    
    public String getMobilePortraitUrl();
    
    public String getAltText();
    
    public String getDefaultImageUrl();
    
    public void setDefaultImageUrl(final String defaultImageUrl);
    
    public String getDesktopUrl();
    
    public String[] getDynamicMediaConfiguration();
    
    public String getTabletLandscapeUrl();

    public String getTabletPortraitUrl();
    
    public String getRootPath();
        
}
