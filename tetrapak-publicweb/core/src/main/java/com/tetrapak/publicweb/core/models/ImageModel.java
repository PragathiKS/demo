package com.tetrapak.publicweb.core.models;

import com.tetrapak.publicweb.core.utils.LinkUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

/**
 * This is a model class for Image component.
 *
 * @author Nitin Kumar
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ImageModel {

    /** The request. */
    @SlingObject
    SlingHttpServletRequest request;

    /** The anchor id. */
    @ValueMapValue
    private String anchorId;

    /** The anchor title. */
    @ValueMapValue
    private String anchorTitle;

    /** The file reference. */
    @ValueMapValue
    private String fileReference;

    /** The alt. */
    @ValueMapValue
    private String alt;

    /** The pw padding. */
    @ValueMapValue
    private String pwPadding;

    /** The link URL. */
    @ValueMapValue
    private String linkURL;

    /** The pw theme. */
    @ValueMapValue
    private String pwTheme;
    

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        linkURL = LinkUtils.sanitizeLink(linkURL, request);
    }

    /**
     * Gets the anchor id.
     *
     * @return the anchor id
     */
    public String getAnchorId() {
        return anchorId;
    }

    /**
     * Gets the anchor title.
     *
     * @return the anchor title
     */
    public String getAnchorTitle() {
        return anchorTitle;
    }

    /**
     * Gets the file reference.
     *
     * @return the file reference
     */
    public String getFileReference() {
        return fileReference;
    }

    /**
     * Gets the alt.
     *
     * @return the alt
     */
    public String getAlt() {
        return alt;
    }

    /**
     * Gets the pw padding.
     *
     * @return the pw padding
     */
    public String getPwPadding() {
        return pwPadding;
    }

    /**
     * Gets the link URL.
     *
     * @return the link URL
     */
    public String getLinkURL() {
        return linkURL;
    }

    /**
     * Gets the pw theme.
     *
     * @return the pw theme
     */
    public String getPwTheme() {
        return pwTheme;
    }
}
