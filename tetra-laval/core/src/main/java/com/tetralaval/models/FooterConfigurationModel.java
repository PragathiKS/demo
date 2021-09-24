package com.tetralaval.models;

import com.tetralaval.models.multifield.FooterLinkModel;
import com.tetralaval.models.multifield.SocialLinkModel;
import com.tetralaval.utils.LinkUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;
import javax.jcr.Node;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The Class FooterConfigurationModel.
 */
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FooterConfigurationModel {

    /** The resource. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The resource. */
    @Inject
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

    /** The logo background. */
    @ValueMapValue
    private String logoBackground;

    /** The social links. */
    @Inject
    @Via("resource")
    private List<SocialLinkModel> socialLinks;

    /** The footer links. */
    @Inject
    @Via("resource")
    private List<FooterLinkModel> footerLinks;

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
        return LinkUtils.sanitizeLink(logoLink, request);
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
     * Gets the logo background.
     *
     * @return the logo background
     */
    public String getLogoBackground() {
        return logoBackground;
    }

    /**
     * Gets the footer link.
     *
     * @return the footer link
     */
    public List<FooterLinkModel> getFooterLinks() {
        List<FooterLinkModel> lists = new ArrayList<>();
        if (Objects.nonNull(footerLinks)) {
            lists.addAll(footerLinks);

            lists = lists.stream().map(this::setFooterLinkModel).collect(Collectors.toList());
        }
        return lists;
    }

    private FooterLinkModel setFooterLinkModel(FooterLinkModel footerLinkModel) {
        String linkPath = footerLinkModel.getLinkPath();

        ResourceResolver resourceResolver = resource.getResourceResolver();
        Resource resourceLink = resourceResolver.resolve(linkPath);
        footerLinkModel.setInternal(resourceLink.adaptTo(Node.class) != null);
        footerLinkModel.setLinkPath(LinkUtils.sanitizeLink(linkPath, request));
        return footerLinkModel;
    }

}
