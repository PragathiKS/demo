package com.tetrapak.publicweb.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;
import java.util.List;

/**
 * The Class NewHeaderConfigurationModel.
 */
@Model(adaptables = {Resource.class} , defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
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

    /** The market title. */
    @ValueMapValue
    private String marketTitle;

    /** The market title. */
    @ValueMapValue @Default(values = "false")
    private Boolean marketSelectorDisabled;

    /** The search page. */
    @ValueMapValue
    private String searchPage;

    @Inject
    private List<LinkModel> secondaryNavigationLinks;

    @Inject
    private List<MainNavigationLinkModel> mainNavigationLinks;

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
     * Gets the login link.
     *
     * @return the login link
     */
    public String getLoginLink() {
        return loginLink;
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
     * Gets the market title.
     *
     * @return the market title
     */
    public String getMarketTitle() {
        return marketTitle;
    }

    /**
     * Gets the search page.
     *
     * @return the search page
     */
    public String getSearchPage() {
        return searchPage;
    }

    /**
     * Get the market selector flag
     * @return
     */
    public Boolean getMarketSelectorDisabled() {
        return marketSelectorDisabled;
    }

    public List<LinkModel> getSecondaryNavigationLinks() {
        return secondaryNavigationLinks;
    }

    public List<MainNavigationLinkModel> getMainNavigationLinks() {
        return mainNavigationLinks;
    }

}
