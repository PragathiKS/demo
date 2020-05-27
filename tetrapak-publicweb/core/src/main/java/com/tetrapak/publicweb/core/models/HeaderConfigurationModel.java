package com.tetrapak.publicweb.core.models;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.tetrapak.publicweb.core.utils.LinkUtils;

/**
 * The Class HeaderConfigurationModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeaderConfigurationModel {

    /** The resource. */
    @Self
    private Resource resource;

    /** The logo image path. */
    @Inject
    private String logoImagePath;

    /** The logo link. */
    @Inject
    private String logoLink;

    /** The logo alt. */
    @Inject
    private String logoAlt;

    /** The login link. */
    @Inject
    private String loginLink;

    /** The login label. */
    @Inject
    private String loginLabel;

    /** The contact link. */
    @Inject
    private String contactLink;

    /** The contact text. */
    @Inject
    private String contactText;

    /** The solution page. */
    @ValueMapValue
    private String solutionPage;

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
        return LinkUtils.sanitizeLink(logoLink);
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
     * Gets the login link.
     *
     * @return the login link
     */
    public String getLoginLink() {
        return LinkUtils.sanitizeLink(loginLink);
    }

    /**
     * Gets the login label.
     *
     * @return the login label
     */
    public String getLoginLabel() {
        return loginLabel;
    }

    /**
     * Gets the contact link.
     *
     * @return the contact link
     */
    public String getContactLink() {
        return LinkUtils.sanitizeLink(contactLink);
    }

    /**
     * Gets the contact text.
     *
     * @return the contact text
     */
    public String getContactText() {
        return contactText;
    }

    /**
     * Gets the solution page.
     *
     * @return the solution page
     */
    public String getSolutionPage() {
        return LinkUtils.sanitizeLink(solutionPage);
    }
}
