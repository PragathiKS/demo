package com.tetrapak.publicweb.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.settings.SlingSettingsService;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

@Model(adaptables = {Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PageLoadAnalyticsModel {

    @Self
    private Resource resource;

    private Page currentPage;

    @Inject
    private SlingSettingsService slingSettingsService;

    private static final String SITE_NAME = "pw";
    private static final String PAGE_LOAD_EVENT = "content-load";
    public static final String TETRAPAK_TAGS_ROOT_PATH = "/content/cq:tags/tetrapak/";

    private String channel = StringUtils.EMPTY;
    private String pageName = StringUtils.EMPTY;
    private String siteLanguage = StringUtils.EMPTY;
    private String siteCountry = StringUtils.EMPTY;
    private String pageType = StringUtils.EMPTY;
    private String digitalData;
    private boolean production;
    private boolean staging;
    private boolean development;
    private StringBuilder siteSection0 = new StringBuilder(StringUtils.EMPTY);
    private StringBuilder siteSection1 = new StringBuilder(StringUtils.EMPTY);
    private StringBuilder siteSection2 = new StringBuilder(StringUtils.EMPTY);
    private StringBuilder siteSection3 = new StringBuilder(StringUtils.EMPTY);
    private StringBuilder siteSection4 = new StringBuilder(StringUtils.EMPTY);
    private static final int COUNTRY_LEVEL = 4;
    private static final int LANGUAGE_LEVEL = 5;

    @PostConstruct
    public void initModel() {
        PageManager pageManager = resource.getResourceResolver().adaptTo(PageManager.class);
        if (null != pageManager) {
            currentPage = pageManager.getContainingPage(resource);
            if (null != currentPage) {
                String templatePath = currentPage.getProperties().get("cq:template", StringUtils.EMPTY);
                pageType = StringUtils.substringAfterLast(templatePath, "/");
            }
        }

        updateLanguageAndCountry();
        updateRunMode();
        updateSiteSections();
        updatePageName();

        digitalData = buildDigitalDataJson();
    }

    private void updateLanguageAndCountry() {
        Page countryPage = currentPage.getAbsoluteParent(COUNTRY_LEVEL - 1);
        if (countryPage != null) {
            siteCountry = countryPage.getName();
            if ("lang-masters".equalsIgnoreCase(siteCountry)) {
                siteCountry = StringUtils.EMPTY;
            }
        }
        Page languagePage = currentPage.getAbsoluteParent(LANGUAGE_LEVEL - 1);
        if (languagePage != null) {
            siteLanguage = languagePage.getName();
        }
    }

    private void updateSiteSections() {
        int siteSectionIndex = LANGUAGE_LEVEL;
        int currentPageIndex = currentPage.getDepth();
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

    private void updateSection4(int siteSectionIndex, int currentPageIndex) {
        if (updateSectionName(siteSectionIndex, currentPageIndex, siteSection3)) {
            siteSectionIndex++;
            updateSectionName(siteSectionIndex, currentPageIndex, siteSection4);
        }
    }

    private void updatePageName() {
        pageName = SITE_NAME + ":" + siteLanguage;
        if (StringUtils.isNotEmpty(siteSection0.toString())) {
            pageName += ":" + siteSection0.toString();
            if (StringUtils.isNotEmpty(siteSection1.toString())) {
                pageName += ":" + siteSection1.toString();
                updateLowerSection();
            }
        }
        if (currentPage.getDepth() > LANGUAGE_LEVEL) {
            pageName += ":" + currentPage.getName();
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

    private boolean updateSectionName(int siteSectionIndex, int currentPageIndex, StringBuilder siteSection) {
        if (siteSectionIndex < currentPageIndex) {
            Page siteSectionPage = currentPage.getAbsoluteParent(siteSectionIndex);
            if (siteSectionPage != null) {
                siteSection.append(siteSectionPage.getName());
                return true;
            }
        }
        return false;
    }

    private void updateRunMode() {
        if (slingSettingsService != null) {
            Set<String> runModes = slingSettingsService.getRunModes();
            if (runModes.contains("prod")) {
                production = true;
            } else if (runModes.contains("stage")) {
                staging = true;
            } else if (runModes.contains("dev") || runModes.contains("qa")) {
                development = true;
            }
        }
    }

    private String buildDigitalDataJson() {

        JsonObject jsonObject = new JsonObject();

        JsonObject pageInfo = new JsonObject();
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


        JsonObject userInfo = new JsonObject();
        userInfo.addProperty("loginStatus", StringUtils.EMPTY);
        userInfo.addProperty("salesForceId", StringUtils.EMPTY);
        userInfo.addProperty("userRoles", StringUtils.EMPTY);
        userInfo.addProperty("userCountryCode", StringUtils.EMPTY);
        userInfo.addProperty("userLanguage", StringUtils.EMPTY);

        JsonObject errorInfo = new JsonObject();
        errorInfo.addProperty("errorcode", getErrorCode());
        errorInfo.addProperty("errortype", getErrorMessage());

        jsonObject.add("pageinfo", pageInfo);
        jsonObject.add("userinfo", userInfo);
        jsonObject.add("error", errorInfo);

        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls()
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

}
