package com.tetrapak.publicweb.core.models.v2;

import com.day.cq.wcm.api.Page;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.models.LinkModel;
import com.tetrapak.publicweb.core.models.MainNavigationLinkModel;
import com.tetrapak.publicweb.core.models.v2.MarketSelectorModel;
import com.tetrapak.publicweb.core.models.MegaMenuConfigurationModel;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import com.tetrapak.publicweb.core.utils.NavigationUtil;
import com.tetrapak.publicweb.core.utils.PageUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.xss.XSSAPI;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * The Class HeaderModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeaderModel {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(HeaderModel.class);

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The logo image path. */
    private String logoImagePath;

    /** The logo link. */
    private String logoLink;

    /** The logo alt. */
    private String logoAlt;

    /** The login link. */
    private String loginLink;

    /** The contact us link. */
    private List<LinkModel> secondaryNavigationLinks;

    private List<MainNavigationLinkModel> mainNavigationLinks;

    /** The contact us alt text. */
    private String contactUsAltText;

    /** The login label. */
    private String loginLabel;

    /** The solution page. */
    private String solutionPage;

    /** The search page. */
    private String searchPage;

    /** The market Selector Flag. */
    private Boolean marketSelectorDisabled;

    /** The mega menu configuration model. */
    private MegaMenuConfigurationModel megaMenuConfigurationModel;

    /** The solution page title. */
    private String solutionPageTitle;

    /** The market page. */
    private Page marketPage;

    /** The language page. */
    private Page languagePage;

    /** The MORE_THAN_ONE_LANGAUGES. */
    private static final int MORE_THAN_ONE_LANGAUGES = 2;
    
    /** The xssApi. */
    @Reference
    private XSSAPI xssApi;
    
    @Inject
    private Page currentPage;

    private String countryTitle;

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        LOGGER.debug("inside init method");
        final String rootPath = LinkUtils.getRootPath(request.getPathInfo());
        languagePage = PageUtil.getCurrentPage(request.getResourceResolver().getResource(rootPath));
        if (languagePage != null && languagePage.getParent() != null) {
            marketPage = languagePage.getParent();
        }
        final String path = rootPath + "/jcr:content/root/responsivegrid/headerconfigurationv";
        final Resource headerConfigurationResource = request.getResourceResolver().getResource(path);
        megaMenuConfigurationModel = NavigationUtil.getMegaMenuConfigurationModel(request, request.getPathInfo());
        if (Objects.nonNull(headerConfigurationResource)) {
            final HeaderConfigurationModel configurationModel = headerConfigurationResource
                    .adaptTo(HeaderConfigurationModel.class);
            if (Objects.nonNull(configurationModel)) {
                logoImagePath = configurationModel.getLogoImagePath();
                logoLink = LinkUtils.sanitizeLink(configurationModel.getLogoLink(),request);
                logoAlt = configurationModel.getLogoAlt();
                loginLabel = configurationModel.getLoginLabel();
                loginLink = LinkUtils.sanitizeLink(configurationModel.getLoginLink(),request);
                searchPage = LinkUtils.sanitizeLink(configurationModel.getSearchPage(),request);
                marketSelectorDisabled = configurationModel.getMarketSelectorDisabled();
                secondaryNavigationLinks = configurationModel.getSecondaryNavigationLinks();
                mainNavigationLinks = configurationModel.getMainNavigationLinks();

                Page countryPage = PageUtil.getCountryPage(currentPage);
		if (Objects.nonNull(countryPage) && !countryPage.getName().equals(PWConstants.LANG_MASTERS)) {
		    countryTitle = countryPage.getProperties().get(PWConstants.PROP_COUNTRY_NAME, "");
		    LOGGER.debug("countryTitle from Page Property: {}", countryTitle);
		    countryTitle = StringUtils.isBlank(countryTitle) ? countryPage.getTitle() : countryTitle;
		    LOGGER.debug("countryTitle: {}", countryTitle);
		}
            }
        }

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
     * Gets the login link.
     *
     * @return the login link
     */
    public String getLoginLink() {
        return loginLink;
    }

    /**
     * Gets the contact text.
     *
     * @return the contact text
     */
    public String getContactUsAltText() {
        return contactUsAltText;
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
    * Gets the mega menu configuration model.
    *
    * @return the mega menu configuration model
    */
   public MegaMenuConfigurationModel getMegaMenuConfigurationModel() {
       return megaMenuConfigurationModel;
    }

    /**
     * Gets the solution page.
     *
     * @return the solution page
     */
    public String getSolutionPage() {
        return LinkUtils.sanitizeLink(solutionPage, request);
    }

    /**
     * Gets the solution page title.
     *
     * @return the solution page title
     */
    public String getSolutionPageTitle() {
        return solutionPageTitle;
    }


    /**
     * @return markets list
     */
    public MarketSelectorModel getMarketList() {
        return request.adaptTo(MarketSelectorModel.class);
    }

    /**
     * @return current language
     */
    public String getCurrentLanguage() {
        if (null != languagePage) {
            return languagePage.getTitle();
        }
        return StringUtils.EMPTY;
    }

    /**
     * @return current market
     */
    public String getCurrentMarket() {
        if (null != marketPage) {
            return marketPage.getTitle();
        }
        return StringUtils.EMPTY;
    }

    /**
     * @return DisplayCurrentLanguage
     */
    public Boolean getDisplayCurrentLanguage() {
        Boolean isDisplayCurrentLanguage = false;
        if (null != marketPage) {
            final Iterator<Page> childPages = marketPage.listChildren();
            int languagesCount = 0;
            while (childPages.hasNext()) {
                childPages.next();
                languagesCount++;
                if (languagesCount >= MORE_THAN_ONE_LANGAUGES) {
                    isDisplayCurrentLanguage = true;
                    break;
                }
            }

        }
        return isDisplayCurrentLanguage;
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
     * checks if market selector is needed 
     * @return marketSelector flag
     */
    public Boolean getMarketSelectorDisabled() {
        return marketSelectorDisabled;
    }
    
    /**
     * Gets the country page's title
     * 
     * @return country title
     */
    public String getCountryTitle() {
	return countryTitle;
    }

    public List<LinkModel> getSecondaryNavigationLinks() {
        return secondaryNavigationLinks;
    }

    public List<MainNavigationLinkModel> getMainNavigationLinks() {
        return mainNavigationLinks;
    }
}
