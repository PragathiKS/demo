package com.tetrapak.publicweb.core.models.multifield;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.tetrapak.publicweb.core.utils.LinkUtils;

/**
 * The Class LinkModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LinkModel {

    /** The link label. */
    @ValueMapValue
    private String linkLabel;

    /** The Link url. */
    @ValueMapValue
    private String linkUrl;

    /**
     * Gets the link label.
     *
     * @return the link label
     */
    public String getLinkLabel() {
        return linkLabel;
    }

    /**
     * Sets the link label.
     *
     * @param linkLabel
     *            the new link label
     */
    public void setLinkLabel(String linkLabel) {
        this.linkLabel = linkLabel;
    }

    /**
     * Gets the link url.
     *
     * @return the link url
     */
    public String getLinkUrl() {
        return LinkUtils.sanitizeLink(linkUrl);
    }

    /**
     * Sets the link url.
     *
     * @param linkUrl
     *            the new link url
     */
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

}
