package com.tetrapak.publicweb.core.models;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

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

}
