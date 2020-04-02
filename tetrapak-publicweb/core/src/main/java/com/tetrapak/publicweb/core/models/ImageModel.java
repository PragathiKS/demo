package com.tetrapak.publicweb.core.models;

import com.tetrapak.publicweb.core.utils.LinkUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;


/**
 * This is a model class for Image component
 *
 * @author Nitin Kumar
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ImageModel {
    @ValueMapValue
    private String anchorId;

    @ValueMapValue
    private String anchorTitle;

    @ValueMapValue
    private String fileReference;

    @ValueMapValue
    private String alt;

    @ValueMapValue
    private String pwPadding;

    @ValueMapValue
    private String linkURL;

    @PostConstruct
    protected void init() {
        linkURL = LinkUtils.sanitizeLink(linkURL);
    }
    public String getAnchorId() {
        return anchorId;
    }

    public String getAnchorTitle() {
        return anchorTitle;
    }

    public String getFileReference() {
        return fileReference;
    }

    public String getAlt() {
        return alt;
    }

    public String getPwPadding() {
        return pwPadding;
    }

    public String getLinkURL() {
        return linkURL;
    }
}
