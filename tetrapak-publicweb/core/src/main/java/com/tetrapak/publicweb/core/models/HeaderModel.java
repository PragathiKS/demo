package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.xss.XSSAPI;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.publicweb.core.beans.LinkBean;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import com.tetrapak.publicweb.core.utils.NavigationUtil;
import com.tetrapak.publicweb.core.utils.PageUtil;

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
    private String contactUsLink;

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

    /** The mega menu links list. */
    private final List<LinkBean> megaMenuLinksList = new ArrayList<>();

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
        final String path = rootPath + "/jcr:content/root/responsivegrid/headerconfiguration";
        final Resource headerConfigurationResource = request.getResourceResolver().getResource(path);
        megaMenuConfigurationModel = NavigationUtil.getMegaMenuConfigurationModel(request, request.getPathInfo());
        if (Objects.nonNull(headerConfigurationResource)) {
            final HeaderConfigurationModel configurationModel = headerConfigurationResource
                    .adaptTo(HeaderConfigurationModel.class);
            if (Objects.nonNull(configurationModel)) {
                logoImagePath = configurationModel.getLogoImagePath();
                logoLink = LinkUtils.sanitizeLink(configurationModel.getLogoLink(),request);
                logoAlt = configurationModel.getLogoAlt();
                contactUsLink = LinkUtils.sanitizeLink(configurationModel.getContactLink(),request);
                contactUsAltText = configurationModel.getContactText();
                loginLabel = configurationModel.getLoginLabel();
                loginLink = LinkUtils.sanitizeLink(configurationModel.getLoginLink(),request);
                solutionPage = configurationModel.getSolutionPage();
                searchPage = LinkUtils.sanitizeLink(configurationModel.getSearchPage(),request);
                marketSelectorDisabled = configurationModel.getMarketSelectorDisabled();
                Page countryPage = PageUtil.getCountryPage(currentPage);
		if (Objects.nonNull(countryPage) && !countryPage.getName().equals(PWConstants.LANG_MASTERS)) {
		    countryTitle = countryPage.getProperties().get(PWConstants.PROP_COUNTRY_NAME, "");
		    LOGGER.debug("countryTitle from Page Property: {}", countryTitle);
		    countryTitle = StringUtils.isBlank(countryTitle) ? countryPage.getTitle() : countryTitle;
		    LOGGER.debug("countryTitle: {}", countryTitle);
		}
            }
            setMegaMenuLinksList(rootPath);
            setSolutionPageTitle();
        }

    }

    /**
     * Sets the mega menu links list.
     *
     * @param rootPath
     *            the new mega menu links list
     */
    public void setMegaMenuLinksList(final String rootPath) {
        final Resource rootResource = request.getResourceResolver().getResource(rootPath);
        if (Objects.nonNull(rootResource)) {
            final ResourceResolver resourceResolver = rootResource.getResourceResolver();
            final PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
            if (Objects.nonNull(pageManager)) {
                final Page page = pageManager.getContainingPage(rootResource);
                setLinkBean(page);
            } else {
                LOGGER.error("Page Manager is null");
            }
        }
    }

    /**
     * Sets the link bean.
     *
     * @param page
     *            the new link bean
     */
    private void setLinkBean(final Page page) {
        if (Objects.nonNull(page)) {
            final Iterator<Page> childPages = page.listChildren();
            while (childPages.hasNext()) {
                populateMegaMenuLinksList(childPages);
            }
        }
    }

    /**
     * Populate mega menu links list.
     *
     * @param childPages
     *            the child pages
     */
    private void populateMegaMenuLinksList(final Iterator<Page> childPages) {
        final Page childPage = childPages.next();
        if (!childPage.isHideInNav()) {
            final LinkBean linkBean = new LinkBean();
            final String title = NavigationUtil.getNavigationTitle(childPage);
            linkBean.setLinkText(title);
            linkBean.setLinkPath(LinkUtils.sanitizeLink(childPage.getPath(), request));
            final String solutionPageWithoutExtension = NavigationUtil.getSolutionPageWithoutExtension(solutionPage);
            if (!childPage.getPath().equalsIgnoreCase(solutionPageWithoutExtension)) {
                final SectionMenuModel sectionMenuModel = new SectionMenuModel();
                sectionMenuModel.setSectionHomePageTitle(childPage);
                sectionMenuModel.setSectionHomePagePath(childPage, request);
                sectionMenuModel.populateSectionMenu(megaMenuConfigurationModel, childPage,
                        solutionPageWithoutExtension, request);
                linkBean.setNavigationConfigurationModel(sectionMenuModel);
            }
            megaMenuLinksList.add(linkBean);
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
     * Gets the contact link.
     *
     * @return the contact link
     */
    public String getContactUsLink() {
        return contactUsLink;
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
     * Gets the mega menu links list.
     *
     * @return the mega menu links list
     */
    public List<LinkBean> getMegaMenuLinksList() {
        return new ArrayList<>(megaMenuLinksList);
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
     * Gets the end to end solution section.
     *
     * @return the end to end solution section
     */
    public List<MegaMenuSolutionModel> getEndToEndSolutionSection() {
        final List<MegaMenuSolutionModel> endToEndSolutionList = new ArrayList<>();
        final List<MegaMenuSolutionModel> endToEndSolution = megaMenuConfigurationModel.getEndToEndSolutionSection();
        if (CollectionUtils.isNotEmpty(endToEndSolution)) {
            endToEndSolution.forEach(f -> {
                f.setPath(LinkUtils.sanitizeLink(f.getPath(), request));
                endToEndSolutionList.add(f);
            });
        }
        return endToEndSolutionList;
    }

    /**
     * Gets the link section.
     *
     * @return the link section
     */
    public List<LinkModel> getLinkSection() {
        final List<LinkModel> linkSectionList = new ArrayList<>();
        final List<LinkModel> linkSection = megaMenuConfigurationModel.getLinkSection();
        if (CollectionUtils.isNotEmpty(linkSection)) {
            linkSection.forEach(f -> {
                f.setLinkUrl(LinkUtils.sanitizeLink(f.getLinkUrl(), request));
                linkSectionList.add(f);
            });
        }
        return linkSectionList;
    }

    /**
     * Gets the food category section.
     *
     * @return the food category section
     */
    public List<MegaMenuSolutionModel> getFoodCategorySection() {
        final List<MegaMenuSolutionModel> foodCategoryList = new ArrayList<>();
        final List<MegaMenuSolutionModel> foodCategorySection = megaMenuConfigurationModel.getFoodCategorySection();
        if (CollectionUtils.isNotEmpty(foodCategorySection)) {
            foodCategorySection.forEach(f -> {
                f.setPath(LinkUtils.sanitizeLink(f.getPath(), request));
                foodCategoryList.add(f);
            });
        }
        return foodCategoryList;
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
     * Sets the solution page title.
     *
     * @param headerConfigurationResource
     *            the new solution page title
     */
    private void setSolutionPageTitle() {
        this.solutionPageTitle = NavigationUtil.getSolutionPageTitle(request, solutionPage);
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
}
