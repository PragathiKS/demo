package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.tetrapak.publicweb.core.models.multifield.FooterLinkModel;
import com.tetrapak.publicweb.core.models.multifield.SocialLinkModel;
import com.tetrapak.publicweb.core.utils.LinkUtils;

/**
 * The Class FooterConfigurationModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FooterConfigurationModel {

    /** The resource. */
    @Self
    private Resource resource;

    /** The logo image path. */
    @ValueMapValue
    private String logoImagePath;

    /** The logo link. */
    @ValueMapValue
    private String logoLink;

    /** The logo alt. */
    @ValueMapValue
    private String logoAlt;

    /** The social links. */
    @Inject
    private List<SocialLinkModel> socialLinks;

    /** The footer links. */
    @Inject
    private List<FooterLinkModel> footerLinks;

    /** The go to top label. */
    @ValueMapValue
    private String goToTopLabel;

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
        return LinkUtils.sanitizeLink(logoLink, resource.getResourceResolver());
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
        final List<SocialLinkModel> lists = new ArrayList<>();
        if (Objects.nonNull(socialLinks)) {
            lists.addAll(socialLinks);
        }
        return lists;

    }

    /**
     * Gets the footer link.
     *
     * @return the footer link
     */
    public List<FooterLinkModel> getFooterLinks() {
        final List<FooterLinkModel> lists = new ArrayList<>();
        if (Objects.nonNull(footerLinks)) {
            lists.addAll(footerLinks);
        }
        return lists;

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
