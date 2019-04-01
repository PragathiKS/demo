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
            if (null != pageContentPath) {
            	pageContentPath = pageContentPath.replace("/en", "/" + locale);
            }
        	if(!pageContentPath.endsWith("/jcr:content")) {
        	pageContentPath= pageContentPath+"/jcr:content/root/responsivegrid";
        	pageReferenceComponents(pageContentPath);
        	}
        	else if(pageContentPath.endsWith("/jcr:content")){
        		pageReferenceComponents(pageContentPath+"/root/responsivegrid");
        	}
        }
    }

    private void pageReferenceComponents(String path) {
    	
    	Resource componentResources =	resourceResolver.getResource(path);
    	if(null != componentResources) {
    		Iterator<Resource> iterators = componentResources.listChildren();
        	while(iterators.hasNext()) {
        		String componentRefPath = iterators.next().getPath();
        		if(filterContentDrivenComponent(componentRefPath))
        		componentsReference.add(componentRefPath);
        		}
    		}
    	}
    
    private boolean filterContentDrivenComponent(String path) {
    	
		boolean isContentDrivenComponent = false;
		if(Objects.nonNull(path) && (path.contains("introscreen") || path.contains("recommendedforyoucar") || path.contains("getstarted"))) {
			isContentDrivenComponent = true;
		}
		return isContentDrivenComponent;
	}

	public List<String> getComponentsReference() {
		
		return componentsReference;
	}

	public String getPageContentPath() {
		
		return pageContentPath;
	}

}
