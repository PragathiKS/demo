package com.tetrapak.customerhub.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RefrencesModel {

    @Self
    private Resource resource;

    @Inject
    private String contentPath;
    
    private String locale;

     @PostConstruct
    protected void init() {
        
    }
     
	/**
	 * Process the path to fetch the locale specific path.
	 * @return processed path
	 */
    public String getContentPath() {
    	String path = contentPath;
    	path = path.replace("/global", "/"+null!=locale?locale:"global");
        return path;
    }
    
}
