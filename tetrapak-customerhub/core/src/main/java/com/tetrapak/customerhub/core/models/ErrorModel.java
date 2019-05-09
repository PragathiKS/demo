package com.tetrapak.customerhub.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import javax.inject.Inject;


/**
 * Model class for Error Component
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ErrorModel {
    
    @Self
    protected Resource resource;

    @Inject
    protected String imagePath;

    @Inject
    protected String imageAltText;
    
    @Inject
    protected String errorTitle;
    
    @Inject
    protected String errorCode;
    
    @Inject
    protected String errorDescription;

    /**
     * @return the imagePath
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * @return the altText
     */
    public String getImageAltText() {
        return imageAltText;
    }

    /**
     * @return the errorTitle
     */
    public String getErrorTitle() {
        return errorTitle;
    }

    /**
     * @return the errorCode
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * @return the errorDescription
     */
    public String getErrorDescription() {
        return errorDescription;
    }
}
