package com.tetralaval.models.multifield;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * The Class SocialLinkModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SocialLinkModel {

    /** The social media. */
    @ValueMapValue
    private String socialMedia;

    /** The social media link path. */
    @ValueMapValue
    private String socialMediaLink;

    /**
     * Gets the social media.
     *
     * @return the social media
     */
    public String getSocialMedia() {
        return socialMedia;
    }

    /**
     * Gets the social media link path.
     *
     * @return the social media link path
     */
    public String getSocialMediaLink() {
        return socialMediaLink;
    }

}
