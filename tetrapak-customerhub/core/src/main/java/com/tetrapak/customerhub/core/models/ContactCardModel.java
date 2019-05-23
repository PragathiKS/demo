package com.tetrapak.customerhub.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;


/**
 * Model class for Contact Card Component
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContactCardModel {
    
    @Self
    private Resource resource;

    @Inject
    private String headingI18n;
    
    @Inject
    private String subHeadingI18n;
    
    @Inject
    private String viewContactBtnTextI18n;
    
    @Inject
    private String imagePath;

    @Inject
    private String imageAltTextI18n;
    
    @Inject
    private String viewContactBtnRedirectUrl;
        

    /**
     * @return the headingI18n
     */
    public String getHeadingI18n() {
        return headingI18n;
    }

    /**
     * @return the subHeadingI18n
     */
    public String getSubHeadingI18n() {
        return subHeadingI18n;
    }

    /**
     * @return the viewContactBtnTextI18n
     */
    public String getViewContactBtnTextI18n() {
        return viewContactBtnTextI18n;
    }

    /**
     * @return the imagePath
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * @return the imageAltTextI18n
     */
    public String getImageAltTextI18n() {
        return imageAltTextI18n;
    }
       
    /**
     * @return the viewContactBtnRedirectUrl
     */
    public String getViewContactBtnRedirectUrl() {
        return viewContactBtnRedirectUrl;
    }

}
