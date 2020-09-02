package com.tetrapak.publicweb.core.models;

import java.util.*;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import com.tetrapak.publicweb.core.beans.CountryLanguageCodeBean;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.utils.GlobalUtil;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import com.tetrapak.publicweb.core.utils.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.settings.SlingSettingsService;
import org.apache.sling.xss.XSSAPI;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PageLoadAnalyticsModel {

    @Self
    private Resource resource;

    /** The current page. */
    private Page currentPage;

    @Inject
    private SlingSettingsService slingSettingsService;
    
    @Inject
    protected XSSAPI xssapi;

    private Boolean hrefLangFlag = Boolean.FALSE;

    private static final String SITE_NAME = "publicweb";
    private static final String PAGE_LOAD_EVENT = "content-load";
    public static final String TETRAPAK_TAGS_ROOT_PATH = "/content/cq:tags/tetrapak/";
    private static final String PW_ERROR_PAGE_TEMPLATE_NAME = "public-web-error-page";
    private static final String X_DEFAULT = "x-default";
    private String channel = StringUtils.EMPTY;
    private String pageName = StringUtils.EMPTY;
    private String siteLanguage = StringUtils.EMPTY;
    private String siteCountry = StringUtils.EMPTY;
    private String pageType = StringUtils.EMPTY;
    private String digitalData;
    private boolean production;
    private boolean staging;
    private boolean development;
    private String productName;
    private final StringBuilder siteSection0 = new StringBuilder(StringUtils.EMPTY);
    private StringBuilder siteSection1 = new StringBuilder(StringUtils.EMPTY);
    private final StringBuilder siteSection2 = new StringBuilder(StringUtils.EMPTY);
    private final StringBuilder siteSection3 = new StringBuilder(StringUtils.EMPTY);
    private final StringBuilder siteSection4 = new StringBuilder(StringUtils.EMPTY);
    private static final int COUNTRY_LEVEL = 4;
    private static final int LANGUAGE_LEVEL = 5;
    private List<CountryLanguageCodeBean> hrefLangValues = new ArrayList<> ();


    @PostConstruct
    public void initModel() {
        final PageManager pageManager = resource.getResourceResolver().adaptTo(PageManager.class);
        if (null != pageManager) {
            currentPage = pageManager.getContainingPage(resource);
            /**
             * Note : Line no 82-89 is just a temporary check and should be removed once SMAR-15151 is completely delivered
             */
            final Resource headerConfigurationResource = resource.getResourceResolver().
                    getResource(LinkUtils.getRootPath(currentPage.getPath()).
                            concat("/jcr:content/root/responsivegrid/headerconfiguration"));
            if(Objects.nonNull(headerConfigurationResource)) {
                final HeaderConfigurationModel configurationModel = headerConfigurationResource
                        .adaptTo(HeaderConfigurationModel.class);
                hrefLangFlag = configurationModel.getHrefLangFlag().equalsIgnoreCase("true");
                if (Objects.nonNull(configurationModel) && hrefLangFlag) {
                    updateHrefLang(currentPage);
                }
            }
            if (null != currentPage) {
                final String templatePath = currentPage.getProperties().get("cq:template", StringUtils.EMPTY);
                pageType = StringUtils.substringAfterLast(templatePath, "/");
            }
        }

        updateLanguageAndCountry();
        updateRunMode();
        updateSiteSections();
        updatePageName();
        updateProductName();

        digitalData = buildDigitalDataJson();
    }

    /**
     * Update product name.
     */
    private void updateProductName() {
        final ProductModel product = resource.adaptTo(ProductModel.class);
        productName = product.getName();
    }

    private void updateLanguageAndCountry() {
        final Page countryPage = currentPage.getAbsoluteParent(COUNTRY_LEVEL - 1);
        if (countryPage != null) {
            siteCountry = countryPage.getName();
            if ("lang-masters".equalsIgnoreCase(siteCountry)) {
                siteCountry = StringUtils.EMPTY;
            }
        }
        final Page languagePage = currentPage.getAbsoluteParent(LANGUAGE_LEVEL - 1);
        if (languagePage != null) {
            siteLanguage = languagePage.getName();
        }
    }

    private void updateSiteSections() {
        int siteSectionIndex = LANGUAGE_LEVEL;
        final int currentPageIndex = currentPage.getDepth();
        if (updateSectionName(siteSectionIndex, currentPageIndex, siteSection0)) {
            channel = siteSection0.toString();
            siteSectionIndex++;
            if (updateSectionName(siteSectionIndex, currentPageIndex, siteSection1)) {
                siteSectionIndex++;
                if (updateSectionName(siteSectionIndex, currentPageIndex, siteSection2)) {
                    siteSectionIndex++;
                    updateSection4(siteSectionIndex, currentPageIndex);
                }
            }
        }
    }

    private void updateSection4(int siteSectionIndex, final int currentPageIndex) {
        if (updateSectionName(siteSectionIndex, currentPageIndex, siteSection3)) {
            siteSectionIndex++;
            updateSectionName(siteSectionIndex, currentPageIndex, siteSection4);
        }
    }

    private void updatePageName() {
        pageName = "pw:" + siteLanguage;
        if (StringUtils.isNotEmpty(siteSection0.toString())) {
            pageName += ":" + siteSection0.toString();
            if (StringUtils.isNotEmpty(siteSection1.toString())) {
                pageName += ":" + siteSection1.toString();
                if (PW_ERROR_PAGE_TEMPLATE_NAME.equalsIgnoreCase(pageType)) {
                    siteSection1 = new StringBuilder(channel);
                }
                updateLowerSection();
            }
        }
    }

    private void updateLowerSection() {
        if (StringUtils.isNotEmpty(siteSection2.toString())) {
            pageName += ":" + siteSection2.toString();
            if (StringUtils.isNotEmpty(siteSection3.toString())) {
                pageName += ":" + siteSection3.toString();
                if (StringUtils.isNotEmpty(siteSection4.toString())) {
                    pageName += ":" + siteSection4.toString();
                }
            }
        }
    }

    private boolean updateSectionName(final int siteSectionIndex, final int currentPageIndex, final StringBuilder siteSection) {
        if (siteSectionIndex < currentPageIndex) {
            final Page siteSectionPage = currentPage.getAbsoluteParent(siteSectionIndex);
            if (siteSectionPage != null) {
                siteSection.append(siteSectionPage.getName());
                return true;
            }
        }
        return false;
    }

    private void updateRunMode() {
        if (slingSettingsService != null) {
            final Set<String> runModes = slingSettingsService.getRunModes();
            if (runModes.contains("prod")) {
                production = true;
            } else if (runModes.contains("stage")) {
                staging = true;
            } else if (runModes.contains("dev") || runModes.contains("qa")) {
                development = true;
            }
        }
    }

    /**
     * This method is used to set hreflang values and page paths
     * @param  currentPage
     */
    private void updateHrefLang (final Page currentPage){
        final String marketRootPath = LinkUtils.getMarketsRootPath(currentPage.getPath());
        Resource marketRootResource = currentPage.getContentResource().getResourceResolver().getResource(marketRootPath);
        if (Objects.nonNull(marketRootResource)) {
            Page marketRootPage = PageUtil.getCurrentPage(marketRootResource);
            if (Objects.nonNull(marketRootPage)) {
                Iterator<Page> marketPages = marketRootPage.listChildren();
                callHrefLangSetter(marketPages);
            }
        }
    }

    /**
     * This method is used to iterate languagePages and call hrefLang setter
     * @param marketPages
     */
    private void callHrefLangSetter (Iterator<Page> marketPages) {
        while (marketPages.hasNext()) {
            Page marketPage = marketPages.next();
            if (!marketPage.getName().equalsIgnoreCase(PWConstants.LANG_MASTERS)) {
                Iterator<Page> languagePages = marketPage.listChildren();
                while (languagePages.hasNext()) {
                    final Page currentLanguagePage = languagePages.next();
                    final String currentPagePathInLoop = getPagePathForCountryLanguage(
                            PageUtil.getCountryPage(currentLanguagePage).getPath(),
                            PageUtil.getLanguageCode(currentLanguagePage),currentPage);
                    final ResourceResolver resourceResolver = resource.getResourceResolver();
                    setHrefLangValues(resourceResolver, PageUtil.getLocaleFromURL(currentLanguagePage), currentPagePathInLoop );
                }
            }
        }
    }

    /**This method is used to get any page in another locale from current page say
     * from path /content/tetrapak/publicweb/gb/en/home to a locale say fr/en, then this will return
     * /content/tetrapak/publicweb/fr/en/home
     *
     * @param countryPagePath
     * @param language
     * @param currentPage
     * @return String valid page path for any locale
     */
    private String getPagePathForCountryLanguage (final String countryPagePath, final String language, final Page currentPage){
        return  countryPagePath.concat(PWConstants.SLASH).concat(language).
                concat(currentPage.getPath().substring(PageUtil.getLanguagePage(currentPage).getPath().length()));
    }

    /**
     * This method is used to set hreflang and its url
     * @param resourceResolver
     * @param locale
     * @param currentPagePathInLoop
     */
    private void setHrefLangValues(final ResourceResolver resourceResolver,final String locale, final String currentPagePathInLoop){
        CountryLanguageCodeBean countryLanguageCodeBean = new CountryLanguageCodeBean();
        final Resource currentResource= resourceResolver.getResource(currentPagePathInLoop);
        if(null != currentResource &&
                (!currentResource.getPath().equalsIgnoreCase(currentPage.getPath()) ||
                        locale.equalsIgnoreCase(PWConstants.GLOBAL_LOCALE))){
            if(!locale.equalsIgnoreCase(PWConstants.GLOBAL_LOCALE)) {
                countryLanguageCodeBean.setLocale(locale);
            } else {
                countryLanguageCodeBean.setLocale(X_DEFAULT);
            }
            countryLanguageCodeBean.setPageUrl(LinkUtils.sanitizeLink(currentResource.getPath(),resourceResolver));
            hrefLangValues.add(countryLanguageCodeBean);
        }

    }

    private String buildDigitalDataJson() {

        final JsonObject jsonObject = new JsonObject();

        final JsonObject pageInfo = new JsonObject();
        pageInfo.addProperty("channel", channel);
        pageInfo.addProperty("pageType", pageType);
        pageInfo.addProperty("pageName", pageName);
        pageInfo.addProperty("siteSection1", siteSection1.toString());
        pageInfo.addProperty("siteSection2", siteSection2.toString());
        pageInfo.addProperty("siteSection3", siteSection3.toString());
        pageInfo.addProperty("siteSection4", siteSection4.toString());
        pageInfo.addProperty("siteCountry", siteCountry);
        pageInfo.addProperty("siteLanguage", siteLanguage);
        pageInfo.addProperty("siteName", SITE_NAME);


        pageInfo.addProperty("event", PAGE_LOAD_EVENT);


        final JsonObject userInfo = new JsonObject();
        userInfo.addProperty("userId", StringUtils.EMPTY);
        userInfo.addProperty("loginStatus", StringUtils.EMPTY);
        userInfo.addProperty("userLanguage", StringUtils.EMPTY);
        userInfo.addProperty("userRole", StringUtils.EMPTY);

        final JsonObject errorInfo = new JsonObject();
        errorInfo.addProperty("errorcode", getErrorCode());
        errorInfo.addProperty("errortype", getErrorMessage());

        if (!StringUtils.isEmpty(productName)) {
            final JsonObject productInfo = new JsonObject();
            productInfo.addProperty("productName", productName);
            jsonObject.add("productInfo", productInfo);
        }

        jsonObject.add("pageinfo", pageInfo);
        jsonObject.add("userinfo", userInfo);
        jsonObject.add("error", errorInfo);

        final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
        return gson.toJson(jsonObject);
    }

    private String getErrorCode() {
        if (resource.getPath().contains("500")) {
            return Integer.toString(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else if (resource.getPath().contains("404")) {
            return Integer.toString(HttpServletResponse.SC_NOT_FOUND);
        } else {
            return "";
        }
    }

    private String getErrorMessage() {
        if (resource.getPath().contains("500")) {
            return "internal server error";
        } else if (resource.getPath().contains("404")) {
            return "resource not found";
        } else {
            return "";
        }
    }

    public boolean isProduction() {
        return production;
    }

    public boolean isStaging() {
        return staging;
    }

    public boolean isDevelopment() {
        return development;
    }

    public String getDigitalData() {
        return digitalData;
    }

    public String getCanonicalURL() {
    	return  xssapi.getValidHref(LinkUtils.sanitizeLink(currentPage.getPath(), resource.getResourceResolver()));
   }
    
   public Boolean isPublisher(){
 	   return GlobalUtil.isPublish();
   }

    public List<CountryLanguageCodeBean> getHreflangValues() {
        return new ArrayList<>(hrefLangValues);
    }

    public Boolean getHrefLangFlag() {
        return hrefLangFlag;
    }
}
