package com.tetralaval.models;

import com.tetralaval.models.multifield.FooterLinkModel;
import com.tetralaval.models.multifield.SocialLinkModel;
import com.tetralaval.utils.LinkUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The Class FooterModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FooterModel {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(FooterModel.class);

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The logo image path. */
    private String logoImagePath;

    /** The logo link. */
    private String logoLink;

    /** The logo alt. */
    private String logoAlt;

    /** The social links. */
    private List<SocialLinkModel> socialLinks;
    
    /** The footer links. */
    private List<FooterLinkModel> footerLinksSanitized = new ArrayList<>();

    /** The go to top label. */
    private String goToTopLabel;

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        final List<FooterLinkModel> footerLinks;
        LOGGER.debug("inside init method");
        final String rootPath = LinkUtils.getRootPath(request.getPathInfo());
//        TODO: uncomment and fix
//        final String path = rootPath + "/jcr:content/root/responsivegrid/footerconfiguration";
//        final Resource footerConfigurationResource = request.getResourceResolver().getResource(path);
//        if (Objects.nonNull(footerConfigurationResource)) {
//            final FooterConfigurationModel configurationModel = footerConfigurationResource
//                    .adaptTo(FooterConfigurationModel.class);
//            if (Objects.nonNull(configurationModel)) {
//                logoImagePath = configurationModel.getLogoImagePath();
//                logoLink = LinkUtils.sanitizeLink(configurationModel.getLogoLink(), request);
//                logoAlt = configurationModel.getLogoAlt();
//                socialLinks = configurationModel.getSocialLinks();
//                footerLinks = configurationModel.getFooterLinks();
//                for(FooterLinkModel footerLink:footerLinks) {
//                    String sanitizedPath = LinkUtils.sanitizeLink(footerLink.getLinkPath(), request);
//                    footerLink.setLinkPath(sanitizedPath);
//                    footerLinksSanitized.add(footerLink);
//                }
//                goToTopLabel = configurationModel.getGoToTopLabel();
//
//            }
//        }
    }

    /**
     * Gets the logo image path.
     *
     * @return the logo image path
     */
    public String getLogoImagePath() {
        return logoImagePath;
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
        return new ArrayList<>(footerLinksSanitized);
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
