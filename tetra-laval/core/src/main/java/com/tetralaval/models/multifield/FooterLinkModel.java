package com.tetralaval.models.multifield;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * The Class FooterLinkModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FooterLinkModel {

    /** The resource. */
    @Self
    private Resource request;

    /** The link label. */
    @ValueMapValue
    private String linkLabel;

    /** The link path. */
    @ValueMapValue
    private String linkPath;

    private boolean internal;

    /**
     * Gets the link label.
     *
     * @return the link label
     */
    public String getLinkLabel() {
        return linkLabel;
    }

    /**
     * Gets the link path.
     *
     * @return the link path
     */
    public String getLinkPath() {
        return linkPath;
    }
    
    /**
     * Sets the link path.
     *
     * @param linkPath the new link path
     */
    public void setLinkPath(String linkPath) {
        this.linkPath=linkPath;
    }

    /**
     * internal getter
     * @return internal
     */
    public boolean isInternal() {
        return internal;
    }

    /**
     * internal setter
     * @param internal
     */
    public void setInternal(boolean internal) {
        this.internal = internal;
    }
}
