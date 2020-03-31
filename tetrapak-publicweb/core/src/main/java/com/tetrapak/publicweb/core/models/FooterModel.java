package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.publicweb.core.models.multifield.FooterLinkModel;
import com.tetrapak.publicweb.core.models.multifield.SocialLinkModel;
import com.tetrapak.publicweb.core.utils.LinkUtils;

/**
 * The Class FooterModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

public class FooterModel {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(HeaderModel.class);

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The logo link. */
    private String logoLink;

    /** The logo link target. */
    private String logoLinkTarget;

    /** The logo alt. */
    private String logoAlt;

    /** The social links. */
    private List<SocialLinkModel> socialLinks;

    /** The footer links. */
    private List<FooterLinkModel> footerLinks;

    /** The go to top label. */
    private String goToTopLabel;

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        LOGGER.debug("inside init method");
        final String rootPath = LinkUtils.getRootPath(request.getPathInfo());
        final String path = rootPath + "/jcr:content/root/responsivegrid/footerconfiguration";
        final Resource footerConfigurationResource = request.getResourceResolver().getResource(path);
        if (Objects.nonNull(footerConfigurationResource)) {
            final FooterConfigurationModel configurationModel = footerConfigurationResource
                    .adaptTo(FooterConfigurationModel.class);
            if (Objects.nonNull(configurationModel)) {
                logoLink = configurationModel.getLogoLink();
                logoLinkTarget = configurationModel.getLogoLinkTarget();
                logoAlt = configurationModel.getLogoAlt();
                socialLinks = configurationModel.getSocialLinks();
                footerLinks = configurationModel.getFooterLinks();
                goToTopLabel = configurationModel.getGoToTopLabel();

            }
        }
    }

    /**
     * Gets the logo link.
     *
     * @return the logo link
     */
    public String getLogoLink() {
        return logoLink;
    }

    /**
     * Gets the logo link target.
     *
     * @return the logo link target
     */
    public String getLogoLinkTarget() {
        return logoLinkTarget;
    }

    /**
     * Gets the logo alt.
     *
     * @return the logo alt
     */
    public String getLogoAlt() {
        return logoAlt;
    }

    /**
     * Gets the social links.
     *
     * @return the social links
     */
    public List<SocialLinkModel> getSocialLinks() {
        return new ArrayList<>(socialLinks);
    }

    /**
     * Gets the footer links.
     *
     * @return the footer links
     */
    public List<FooterLinkModel> getFooterLinks() {
        return new ArrayList<>(footerLinks);
    }

    /**
     * Gets the go to top label.
     *
     * @return the go to top label
     */
    public String getGoToTopLabel() {
        return goToTopLabel;
    }
}
