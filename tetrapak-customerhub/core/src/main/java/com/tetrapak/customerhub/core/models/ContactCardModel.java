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
    private String heading;
    
    @Inject
    private String subHeading;
    
    @Inject
    private String viewContactBtnText;
    
    @Inject
    private String imagePath;

    @Inject
    private String imageAltText;
        
    private String i18nKeys;
    
    
    /**
     * init method
     * @return config 
     */
    @PostConstruct
    protected void init() {
   
        Map<String, Object> i18KeyMap = new HashMap<String, Object>();
        i18KeyMap.put("heading", getHeading());
        i18KeyMap.put("subHeading", getSubHeading());
        i18KeyMap.put("viewcontactbtntext", getViewContactBtnText());
        i18KeyMap.put("imagePath", getImagePath());
        i18KeyMap.put("imageAltText", getImageAltText());
        
        Gson gson = new Gson();
        i18nKeys = gson.toJson(i18KeyMap);
       
    }
    
    /**
     * @return the heading
     */
    public String getHeading() {
        return heading;
    }

    /**
     * @return the subHeading
     */
    public String getSubHeading() {
        return subHeading;
    }

    /**
     * @return the viewContactBtnText
     */
    public String getViewContactBtnText() {
        return viewContactBtnText;
    }

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
     * @return the i18nKeys
     */
    public String getI18nKeys() {
        return i18nKeys;
    }

}
