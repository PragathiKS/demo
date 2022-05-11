package com.tetrapak.customerhub.core.models.multifield;

import com.tetrapak.customerhub.core.models.LinkModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;

/**
 * The Class ManualModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ManualModel {

    /** The resource. */
    @Self
    private Resource resource;

    /** The title. */
    @ValueMapValue
    private String title;

    /** The description. */
    @ValueMapValue
    private String description;

    /** The link path. */
    @Inject
    @Via("resource")
    private LinkModel link;

    /** The file reference. */
    @ValueMapValue
    private String fileReference;

    /** The alt. */
    @ValueMapValue
    private String alt;
    
    /** The asset name. */
    private String assetName;
    
    /** The Constant FORWARD_SLASH. */
    private static final String FORWARD_SLASH = "/";

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
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
     * Gets the link.
     *
     * @return the link text
     */
    public LinkModel getLink() {
        return link;
    }

    /**
     * Sets the link.
     *
     * @param link the new link
     */
    public void setLink(LinkModel link) {
        this.link = link;
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
     * Gets the asset name.
     *
     * @return the asset name
     */
    public String getAssetName() {
        assetName = StringUtils.EMPTY;
        if (link != null) {
            assetName = StringUtils.substringAfterLast(link.getLinkUrl(), FORWARD_SLASH);
        }
        return assetName;
    }

}
