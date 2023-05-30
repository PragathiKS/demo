package com.tetrapak.publicweb.core.models.multifield;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
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
    
    /** The Link Desc. */
    @ValueMapValue
    private String linkDesc;

    /** The asset name. */
    private String assetName;

    /** The Constant FORWARD_SLASH. */
    private static final String FORWARD_SLASH = "/";

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
     * Gets the link Desc.
     *
     * @return the link Desc
     */
    public String getLinkDesc() {
        return linkDesc;
    }

    /**
     * Sets the link Desc.
     *
     * @param linkDesc
     *            the new link Desc
     */
    public void setLinkDesc(String linkDesc) {
        this.linkDesc = linkDesc;
    }

    /**
     * Gets the link url.
     *
     * @return the link url
     */
    public String getLinkUrl() {
        return linkUrl;
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

    /**
     * Gets the asset name.
     *
     * @return the asset name
     */
    public String getAssetName() {
        assetName = StringUtils.EMPTY;
        if (StringUtils.isNotEmpty(linkUrl)) {
            assetName = StringUtils.substringAfterLast(linkUrl, FORWARD_SLASH);
        }
        return assetName;
    }

}
