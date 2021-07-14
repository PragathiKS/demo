package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

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
    
    /** The current page. */
    @Inject
    private Page currentPage;

    /** The markets. */
    private List<MarketBean> markets = new ArrayList<>();

    /** The market title. */
    private String marketTitle;

    /** The language count. */
    private int languageCount;

    /** The market rows. */
    private int marketRows;

    /** The Constant TOTAL_COLUMNS. */
    private static final int TOTAL_COLUMNS = 4;

    /** The Constant COLUMN2. */
    private static final int COLUMN2 = 2;

    /** The Constant COLUMN3. */
    private static final int COLUMN3 = 3;

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        LOGGER.debug("MarketSelectorModel::inside init method");
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
     * @return global market
     */
    public MarketBean getGlobalMarket() {
        MarketBean globalMarketBean = new MarketBean();
        LanguageBean globalLanguageBean = new LanguageBean();
        globalLanguageBean.setLanguageIndex(1);
        globalLanguageBean.setLanguageName(PageUtil
                .getCurrentPage(request.getResourceResolver().getResource(
                        PWConstants.GLOBLA_MARKET_PATH + PWConstants.SLASH + PWConstants.ENGLISH_LANGUAGE_ISO_CODE))
                .getTitle());
        globalLanguageBean.setLinkPath(getGlobalMarketPath());
        List<LanguageBean> globalLanguages = new ArrayList<>();
        globalLanguages.add(globalLanguageBean);
        globalMarketBean.setLanguages(globalLanguages);
        globalMarketBean.setMarketName(getGlobalMarketTitle());
        final Page countryPage = PageUtil.getCountryPage(currentPage);
        if (Objects.nonNull(countryPage) && getGlobalMarketTitle().equalsIgnoreCase(
                (String) countryPage.getTitle())) {
            globalMarketBean.setActive(Boolean.TRUE);
        }
        return globalMarketBean;
    }

    /**
     * Process markets.
     *
     * @param marketPages
     *            the market pages
     */
    private void processMarkets(Iterator<Page> marketPages) {
        if (Objects.nonNull(request.getResourceResolver().getResource(
                PWConstants.GLOBLA_MARKET_PATH + PWConstants.SLASH + PWConstants.ENGLISH_LANGUAGE_ISO_CODE))) {
            languageCount++;
        }
        while (marketPages.hasNext()) {
            Page marketPage = marketPages.next();
            if (!marketPage.getName().equalsIgnoreCase(PWConstants.GLOBAL_ISO_CODE)
                    && !marketPage.getName().equalsIgnoreCase(PWConstants.LANG_MASTERS)) {
                Iterator<Page> languagePages = marketPage.listChildren();
                MarketBean marketBean = new MarketBean();
                marketBean.setMarketName(marketPage.getTitle());
                if (marketPage.getContentResource().getValueMap().containsKey("countryName")){
                    marketBean.setCountryName((String)marketPage.getContentResource().getValueMap().get("countryName"));
                }
                final Page countryPage = PageUtil.getCountryPage(currentPage);
                if(Objects.nonNull(countryPage) && marketPage.getTitle().equalsIgnoreCase(
                        (String) countryPage.getTitle())) {
                        marketBean.setActive(Boolean.TRUE);                
                }              
                List<LanguageBean> languages = new ArrayList<>();
                while (languagePages.hasNext()) {
                    languageCount++;
                    Page languagePage = languagePages.next();
                    LanguageBean languageBean = new LanguageBean();
                    languageBean.setLanguageName(languagePage.getTitle());
                    languageBean.setLinkPath(LinkUtils
                            .sanitizeLink(languagePage.getPath() + PWConstants.SLASH + PWConstants.HOME_PAGE_REL_PATH,
                                    request));
                    if (languagePage.getContentResource().getValueMap().containsKey("countryTitle")){
                        languageBean.setCountryTitle((String)languagePage.getContentResource().getValueMap().get("countryTitle"));
                    }
                    languages.add(languageBean);
                }
                marketBean.setLanguages(languages);
                markets.add(marketBean);
            }
        }
        if (languageCount % TOTAL_COLUMNS == 0) {
            marketRows = languageCount / TOTAL_COLUMNS;
        } else {
            marketRows = languageCount / TOTAL_COLUMNS + 1;
        }
    }

    /**
     * Gets the markets.
     *
     * @return the markets
     */
    public List<MarketBean> getMarkets() {
        List<MarketBean> finalMarkets = new ArrayList<>();
        int index = 0;
        if (Objects.nonNull(request.getResourceResolver().getResource(
                PWConstants.GLOBLA_MARKET_PATH + PWConstants.SLASH + PWConstants.ENGLISH_LANGUAGE_ISO_CODE))) {
            finalMarkets.add(getGlobalMarket());
            index++;
        }
        if (null != markets && !markets.isEmpty()) {
            Collections.sort(markets);
            for (int i = 0; i < markets.size(); i++) {
                for (int j = 0; j < markets.get(i).getLanguages().size(); j++) {
                    index++;
                    markets.get(i).getLanguages().get(j).setLanguageIndex(index);
                }
            }
            finalMarkets.addAll(markets);
        }
        return new ArrayList<>(finalMarkets);
    }

    /**
     * Gets the global market path.
     *
     * @return the global market path
     */
    public String getGlobalMarketPath() {
        return LinkUtils.sanitizeLink(PWConstants.GLOBLA_MARKET_PATH + PWConstants.SLASH
                + PWConstants.ENGLISH_LANGUAGE_ISO_CODE + PWConstants.SLASH + PWConstants.HOME_PAGE_REL_PATH, request);
    }

    /**
     * Gets the global market title.
     *
     * @return the global market title
     */
    public String getGlobalMarketTitle() {
        if (Objects.nonNull(request.getResourceResolver().getResource(PWConstants.GLOBLA_MARKET_PATH))) {
            return PageUtil.getCurrentPage(request.getResourceResolver().getResource(PWConstants.GLOBLA_MARKET_PATH))
                    .getTitle();
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

    /**
     * Gets the col 1 end.
     *
     * @return the col 1 end
     */
    public int getCol1End() {
        return marketRows;
    }

    /**
     * Gets the col 2 start.
     *
     * @return the col 2 start
     */
    public int getCol2Start() {
        return marketRows + 1;
    }

    /**
     * Gets the col 2 end.
     *
     * @return the col 2 end
     */
    public int getCol2End() {
        return marketRows * COLUMN2;
    }

    /**
     * Gets the col 3 start.
     *
     * @return the col 3 start
     */
    public int getCol3Start() {
        return marketRows * COLUMN2 + 1;
    }

    /**
     * Gets the col 3 end.
     *
     * @return the col 3 end
     */
    public int getCol3End() {
        return marketRows * COLUMN3;
    }

    /**
     * Gets the col 4 start.
     *
     * @return the col 4 start
     */
    public int getCol4Start() {
        return marketRows * COLUMN3 + 1;
    }

    /**
     * Gets the col 4 end.
     *
     * @return the col 4 end
     */
    public int getCol4End() {
        return languageCount;
    }

}
