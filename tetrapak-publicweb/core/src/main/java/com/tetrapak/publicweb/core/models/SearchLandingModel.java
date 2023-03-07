package com.tetrapak.publicweb.core.models;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

/**
 * The Class SearchLandingModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SearchLandingModel {
    
    /** The isEvent flag. */
    @Inject
    private String isEvent;

    /** The isCasesArticles flag. */
    @Inject
    private String isCasesArticles;
    
    /** The heading. */
    @Inject
    private String heading;
    
    /** The description. */
    @Inject
    private String description;
    
    /** The resource. */
    @Self
    private Resource resource;

    /**
     * Gets the isEvent flag.
     *
     * @return the isEvent flag
     */
	public String getIsEvent() {
		return isEvent;
	}

	/**
     * Gets the isCasesArticles flag.
     *
     * @return the isCasesArticles flag
     */
	public String getIsCasesArticles() {
		return isCasesArticles;
	}

	/**
     * Gets the heading.
     *
     * @return the heading
     */
	public String getHeading() {
		return heading;
	}

	/**
     * Gets the description.
     *
     * @return the description
     */
	public String getDescription() {
		return description;
	}
	
	/**
     * Gets the servlet path.
     *
     * @return the servlet path
     */
    public String getServletPath() {
        return resource.getPath();
    }
}
