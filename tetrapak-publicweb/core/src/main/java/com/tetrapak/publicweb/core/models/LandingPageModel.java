package com.tetrapak.publicweb.core.models;

import javax.annotation.PostConstruct; 

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.solr.common.SolrInputDocument;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LandingPageModel extends BasePageModel {

	private ValueMap jcrMap;
	
	private String title;
	private String vanityDescription;
	private String ctaTexti18nKey;
	private Boolean openInNewWindow;
	
    @PostConstruct
    public void init() {
        super.init();
 
        jcrMap = super.getPageContent().getJcrMap();
        
        if (jcrMap != null) {
        	tile = jcrMap.get("title", String.class);
        	vanityDescription = jcrMap.get("vanityDescription", String.class);
        	ctaTexti18nKey = jcrMap.get("ctaTexti18nKey", String.class);
        	openInNewWindow = jcrMap.get("openInNewWindow", Boolean.class);
        }
    }

    public String getTitle() {
        return title;
    }

    public String getVanityDescription() {
        return vanityDescription;
    }
    
    public String getCtaTexti18nKey() {
        return ctaTexti18nKey;
    }
    
    public Boolean isOpenInNewWindow() {
        return openInNewWindow;
    }
}
