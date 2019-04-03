package com.tetrapak.customerhub.core.models;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Model class for page reference components
 */
@Model(adaptables = Resource.class)
public class PageReferencesModel {

    @SlingObject
    private ResourceResolver resourceResolver;
	
    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    private String pageContentPath;
    
    List<String> componentsReference = new LinkedList<String>();
    private String locale;
    
	@PostConstruct
    protected void init() {
        if(Objects.nonNull(pageContentPath)) {
        	locale = StringUtils.isNotBlank(locale) ? locale : "en";
        	String pagePath = new String(pageContentPath); 
            pagePath = pagePath.replace("/en", "/" + locale);
        	if(!pagePath.endsWith("/jcr:content")) {
        		pagePath = pagePath+"/jcr:content/root/responsivegrid";
        	    pageReferenceComponents(pagePath);
        	}
        	else if(pagePath.endsWith("/jcr:content")){
        		pageReferenceComponents(pagePath+"/root/responsivegrid");
        	}
        }
    }

    private void pageReferenceComponents(String path) {
    	Resource componentResources = resourceResolver.getResource(path);
    	if(Objects.nonNull(componentResources)) {
    		Iterator<Resource> iterators = componentResources.listChildren();
        	while(iterators.hasNext()) {
        		componentsReference.add(iterators.next().getPath());
        		}
    		}
    	}

	public List<String> getComponentsReference() {
		return componentsReference;
	}

	public String getPageContentPath() {
		return pageContentPath;
	}

}
