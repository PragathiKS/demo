package com.tetrapak.publicweb.core.models;

import com.tetrapak.publicweb.core.utils.LinkUtils;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * The Class LinkModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LinkModel {

    /** The request. */
    @Self
    private SlingHttpServletRequest request;
    
    /** The link text. */
    @ValueMapValue
    private String linkText;

    /** The link path. */
    @ValueMapValue
    private String linkUrl;

    /**
     * Gets the link text.
     *
     * @return the link text
     */
    public String getLinkText() {
        return linkText;
    }

    /**
     * Gets the link url.
     *
     * @return the link url
     */
    public String getLinkUrl() {
        return LinkUtils.sanitizeLink(linkUrl, request);
    }
}
