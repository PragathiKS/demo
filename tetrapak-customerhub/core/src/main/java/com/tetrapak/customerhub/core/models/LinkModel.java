package com.tetrapak.customerhub.core.models;

import javax.annotation.PostConstruct;

import com.tetrapak.customerhub.core.utils.LinkUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.commons.io.FilenameUtils;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
/**
 * The Class LinkModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LinkModel {

    /** The link text. */
    @ValueMapValue
    private String linkText;

    /** The link path. */
    @ValueMapValue
    private String linkUrl;

    /** The link type. */
    @ValueMapValue
    private String linkType;

    /** The asset name. */
    private String assetName;

    @PostConstruct
    protected void init() {
	if (StringUtils.isNotEmpty(linkUrl)) {
	    linkType = LinkUtil.checkLinkType(linkUrl);
        if (StringUtils.equals(linkType, CustomerHubConstants.DOWNLOAD_LINK)) {
            linkType = checkPDF(linkType);
        }
	    if (StringUtils.equals(linkType, CustomerHubConstants.DOWNLOAD_LINK)) {
		assetName = LinkUtil.getAssetName(linkUrl);
	    }
	}
    }

    private String checkPDF(String linkType) {
        if(FilenameUtils.getExtension(linkUrl).matches(CustomerHubConstants.PDF)){
            return linkType = CustomerHubConstants.PDF;
        }
        else{
            return linkType;
        }

    }

    /**
     * Gets the asset name.
     *
     * @return the asset name
     */
    public String getAssetName() {
	    return assetName;
    }

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
        return linkUrl;
    }

    /**
     * Gets the link type.
     *
     * @return the link type
     */
    public String getLinkType() {
        return linkType;
    }

    /**
     * Sets the linkUrl.
     *
     * @param linkUrl
     *            the new linkUrl
     */
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    /**
     * Sets the link type.
     *
     * @param linkType the new link type
     */
    public void setLinkType(String linkType) {
	    this.linkType = linkType;
    }
}
