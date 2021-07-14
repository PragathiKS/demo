package com.tetrapak.publicweb.core.models;

import com.tetrapak.publicweb.core.models.multifield.SocialLinkModel;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

/**
 * The Class FooterModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SocialSidebarModel {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SocialSidebarModel.class);

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The social media. */
    private String socialMedia;

    /** The social media link. */
    private String socialMediaLink;

    /** QR code */
    private String wechatQrCodeReference;

    /** Alt Text*/
    private String qrAltText;

    /** Title*/
    private String qrCodeTitle;

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        LOGGER.debug("inside init method");
        final String rootPath = LinkUtils.getRootPath(request.getPathInfo());
        final String path = rootPath + "/jcr:content/root/responsivegrid/socialsidebarconfig";
        final Resource resource = request.getResourceResolver().getResource(path);
        if (Objects.nonNull(resource)) {
            final SocialSideBarConfigurationModel configurationModel = resource.adaptTo(SocialSideBarConfigurationModel.class);
            if (Objects.nonNull(configurationModel)) {
                socialMedia = configurationModel.getSocialMedia();
                socialMediaLink = configurationModel.getSocialMediaLink();
                wechatQrCodeReference = configurationModel.getWechatQrCodeReference();
                qrAltText = configurationModel.getQrAltText();
                qrCodeTitle =configurationModel.getQrCodeTitle();
            }
        }
    }

    /**
     * Gets the social media.
     *
     * @return the social media
     */
    public String getSocialMedia() {
        return socialMedia;
    }

    /**
     * Gets the social media link.
     *
     * @return the social media
     */
    public String getSocialMediaLink() {
        return socialMediaLink;
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
