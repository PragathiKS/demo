package com.tetrapak.publicweb.core.models;


import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.models.multifield.FooterLinkModel;
import com.tetrapak.publicweb.core.models.multifield.SocialLinkModel;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SocialSideBarConfigurationModel {

    /** The request. */
    @Self
    private Resource resource;

    /** The social media. */
    @ValueMapValue
    private String socialMedia;

    /** The social media. */
    @ValueMapValue
    private String socialMediaLink;

    /** QR code */
    @ValueMapValue
    private String wechatQrCodeReference;

    /** Alt Text*/
    @ValueMapValue
    private String qrAltText;

    /** title */
    @ValueMapValue
    private String qrCodeTitle;

    /**
     * Gets the social media links.
     *
     * @return the social media links
     */
    public String getSocialMediaLink() {
        return socialMediaLink;
    }

    /**
     * Gets the social media .
     *
     * @return the social media
     */
    public String getSocialMedia() {
        return socialMedia;
    }

    /**
     * Gets the We chat QR code.
     *
     * @return the We chat QR code
     */
    public String getWechatQrCodeReference() {
        return wechatQrCodeReference;
    }

    /**
     * Gets the Image Alt text.
     *
     * @return the Image Alt text
     */
    public String getQrAltText() {
        return qrAltText;
    }

    /**
     * Gets the Title.
     *
     * @return the Title
     */
    public String getQrCodeTitle() {
        return qrCodeTitle;
    }
}