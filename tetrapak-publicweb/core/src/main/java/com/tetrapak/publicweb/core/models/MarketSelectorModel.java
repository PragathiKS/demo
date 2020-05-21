package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.tetrapak.publicweb.core.beans.LanguageBean;
import com.tetrapak.publicweb.core.beans.MarketBean;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import com.tetrapak.publicweb.core.utils.PageUtil;

/**
 * The Class HeaderModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MarketSelectorModel {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MarketSelectorModel.class);

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The markets. */
    private List<MarketBean> markets = new ArrayList<>();

    /** The market title. */
    private String marketTitle;

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        LOGGER.debug("inside init method");
        final String rootPath = LinkUtils.getMarketsRootPath(request.getPathInfo());
        Resource marketRootRes = request.getResourceResolver().getResource(rootPath);
        if (Objects.nonNull(marketRootRes)) {
            Page marketRootPage = PageUtil.getCurrentPage(marketRootRes);
            if (Objects.nonNull(marketRootPage)) {
                processMarkets(marketRootPage.listChildren());
            }
        }
    }
    

    /**
     * Process markets.
     *
     * @param marketPages
     *            the market pages
     */
    private void processMarkets(Iterator<Page> marketPages) {
        int languageCount = 0;
        while (marketPages.hasNext()) {
            Page marketPage = marketPages.next();
            if (!marketPage.getName().equalsIgnoreCase(PWConstants.GB_ISO_CODE)
                    && !marketPage.getName().equalsIgnoreCase(PWConstants.LANG_MASTERS)) {
                Iterator<Page> languagePages = marketPage.listChildren();
                MarketBean marketBean = new MarketBean();
                marketBean.setMarketName(marketPage.getPageTitle());
                List<LanguageBean> languages = new ArrayList<>();
                while (languagePages.hasNext()) {
                    languageCount++;
                    Page languagePage = languagePages.next();
                    LanguageBean languageBean = new LanguageBean();
                    languageBean.setLanguageName(languagePage.getPageTitle());
                    languageBean.setLanguageIndex(languageCount);
                    languageBean.setLinkPath(LinkUtils
                            .sanitizeLink(languagePage.getPath() + PWConstants.SLASH + PWConstants.HOME_PAGE_REL_PATH));
                    languages.add(languageBean);
                }
                marketBean.setLanguages(languages);
                markets.add(marketBean);
            }
        }
    }

    /**
     * Gets the markets.
     *
     * @return the markets
     */
    public List<MarketBean> getMarkets() {
        Collections.sort(markets);
        return new ArrayList<>(markets);
    }

    /**
     * Gets the global market path.
     *
     * @return the global market path
     */
    public String getGlobalMarketPath() {
        return LinkUtils.sanitizeLink(PWConstants.GLOBLA_MARKET_PATH + PWConstants.SLASH
                + PWConstants.ENGLISH_LANGUAGE_ISO_CODE + PWConstants.SLASH + PWConstants.HOME_PAGE_REL_PATH);
    }

    /**
     * Gets the global market title.
     *
     * @return the global market title
     */
    public String getGlobalMarketTitle() {
        if (Objects.nonNull(request.getResourceResolver().getResource(PWConstants.GLOBLA_MARKET_PATH))) {
            return PageUtil.getCurrentPage(request.getResourceResolver().getResource(PWConstants.GLOBLA_MARKET_PATH))
                    .getPageTitle();
        }
        return StringUtils.EMPTY;
    }

    /**
     * Gets the market title.
     *
     * @return the market title
     */
    public String getMarketTitle() {
        final String languagePath = LinkUtils.getRootPath(request.getPathInfo())
                + "/jcr:content/root/responsivegrid/headerconfiguration";
        final Resource headerConfigurationResource = request.getResourceResolver().getResource(languagePath);
        if (Objects.nonNull(headerConfigurationResource)) {
            final HeaderConfigurationModel configurationModel = headerConfigurationResource
                    .adaptTo(HeaderConfigurationModel.class);
            if (Objects.nonNull(configurationModel)) {
                marketTitle = configurationModel.getMarketTitle();
            }
        }
        return marketTitle;
    }
}
