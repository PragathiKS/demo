package com.tetrapak.publicweb.core.models.multifield;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.tetrapak.publicweb.core.utils.LinkUtils;

/**
 * The Class FooterLinkModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FooterLinkModel {

    /** The link label. */
    @ValueMapValue
    private String linkLabel;

    /** The link path. */
    @ValueMapValue
    private String linkPath;

    /** The link target. */
    @ValueMapValue
    private String linkTarget;

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
        return LinkUtils.sanitizeLink(linkPath);
    }

    /**
     * Gets the link target.
     *
     * @return the link target
     */
    public String getLinkTarget() {
        return linkTarget;
    }

}
