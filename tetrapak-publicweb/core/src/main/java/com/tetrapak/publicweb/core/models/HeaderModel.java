package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.publicweb.core.beans.LinkBean;
import com.tetrapak.publicweb.core.utils.LinkUtils;
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

    /** The logo link target. */
    private String logoLinkTarget;

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

    /** The mega menu links list. */
    private final List<LinkBean> megaMenuLinksList = new ArrayList<>();

    /** The mega menu configuration model. */
    private MegaMenuConfigurationModel megaMenuConfigurationModel = new MegaMenuConfigurationModel();

    /** The solution page title. */
    private String solutionPageTitle;

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        LOGGER.debug("inside init method");
        final String rootPath = LinkUtils.getRootPath(request.getPathInfo());
        final String path = rootPath + "/jcr:content/root/responsivegrid/headerconfiguration";
        final Resource headerConfigurationResource = request.getResourceResolver().getResource(path);
        if (Objects.nonNull(headerConfigurationResource)) {
            final HeaderConfigurationModel configurationModel = headerConfigurationResource
                    .adaptTo(HeaderConfigurationModel.class);
            if (Objects.nonNull(configurationModel)) {
                logoImagePath = configurationModel.getLogoImagePath();
                logoLink = configurationModel.getLogoLink();
                logoLinkTarget = configurationModel.getLogoLinkTarget();
                logoAlt = configurationModel.getLogoAlt();
                contactUsLink = configurationModel.getContactLink();
                contactUsAltText = configurationModel.getContactText();
                loginLabel = configurationModel.getLoginLabel();
                loginLink = configurationModel.getLoginLink();
                solutionPage = configurationModel.getSolutionPage();
            }
            setMegaMenuLinksList(rootPath);
            setSolutionPageTitle();
        }
        populateMegaMenuConfigurationModel();
    }

    /**
     * Populate mega menu configuration model.
     */
    private void populateMegaMenuConfigurationModel() {
        final String rootPath = LinkUtils.getRootPath(request.getPathInfo());
        final String pagePath = rootPath + "/jcr:content/root/responsivegrid/megamenuconfig";
        final Resource megaMenuConfigResource = request.getResourceResolver().getResource(pagePath);
        if (Objects.nonNull(megaMenuConfigResource)) {
            megaMenuConfigurationModel = megaMenuConfigResource.adaptTo(MegaMenuConfigurationModel.class);
        }
    }

    /**
     * Sets the mega menu links list.
     *
     * @param rootPath
     *            the new mega menu links list
     */
    public void setMegaMenuLinksList(String rootPath) {
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
    private void setLinkBean(Page page) {
        if (Objects.nonNull(page)) {
            final Iterator<Page> childPages = page.listChildren();
            while (childPages.hasNext()) {
                final Page childPage = childPages.next();
                if (!childPage.isHideInNav()) {
                    final LinkBean linkBean = new LinkBean();
                    String title = getTitle(childPage);
                    linkBean.setLinkText(title);
                    linkBean.setLinkPath(LinkUtils.sanitizeLink(childPage.getPath()));
                    megaMenuLinksList.add(linkBean);
                }
            }
        }
    }
    
    /**
     * @param childPage
     * @return title
     */
    private String getTitle(Page childPage) {
        String title = childPage.getNavigationTitle();
        if(StringUtils.isBlank(title)) {
            title = childPage.getTitle();
        }
        return title;
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
     * Gets the solution page.
     *
     * @return the solution page
     */
    public String getSolutionPage() {
        return solutionPage;
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
     * @param headerConfigurationResource the new solution page title
     */
    private void setSolutionPageTitle() {
        final String solutionPageJcrContentPath = StringUtils.substringBefore(solutionPage, ".") + "/jcr:content";
        final Resource solutionPageResource = request.getResourceResolver().getResource(solutionPageJcrContentPath);
        if (Objects.nonNull(solutionPageResource)) {
            final ValueMap properties = solutionPageResource.adaptTo(ValueMap.class);
            solutionPageTitle = properties.get(JcrConstants.JCR_TITLE, StringUtils.EMPTY);
        }
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
        final String languagePath = LinkUtils.getRootPath(request.getPathInfo());
        final Resource languageResource = request.getResourceResolver().getResource(languagePath);
        if (null != languageResource && Objects.nonNull(PageUtil.getCurrentPage(languageResource))) {
            return PageUtil.getCurrentPage(languageResource).getTitle();
        }
        return StringUtils.EMPTY;
    }
    
    /**
     * @return current market
     */
    public String getCurrentMarket() {
        final String languagePath = LinkUtils.getRootPath(request.getPathInfo());
        final Resource languageResource = request.getResourceResolver().getResource(languagePath);
        if (null != languageResource && Objects.nonNull(PageUtil.getCurrentPage(languageResource))
                && Objects.nonNull(PageUtil.getCurrentPage(languageResource).getParent())) {
            return PageUtil.getCurrentPage(languageResource).getParent().getTitle();
        }
        return StringUtils.EMPTY;
    }
}