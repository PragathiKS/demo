package com.tetralaval.models.analytics;

import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.tetralaval.constants.TLConstants;
import com.tetralaval.utils.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.vault.util.Text;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.settings.SlingSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AdobeAnalyticsModel {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdobeAnalyticsModel.class);

    private static final String COLON = ":";
    private static final String DEV_RUN_MODE = "dev";
    private static final String QA_RUN_MODE = "qa";
    private static final String STAGE_RUN_MODE = "stage";
    private static final String PROD_RUN_MODE = "prod";
    private static final String CONTENT_LOAD_EVENT = "content-load";
    private static final String LOGIN_STATUS = "logged-in";
    private static final String ERROR_PAGE_TEMPLATE_NAME = "error-page-template";
    private static final String ERROR_VALUE = "error";
    private static final String ERROR_PAGE_TYPE_VALUE = "errorPage";
    private static final String[] ERROR_CODES_ARRAY = new String[]{
            String.valueOf(HttpServletResponse.SC_NOT_FOUND),
            String.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    };

    @SlingObject
    private SlingHttpServletRequest request;

    @OSGiService
    private SlingSettingsService slingSettingsService;

    private Map<String, String> errorTypesMap;

    private Resource resource;
    private ResourceResolver resourceResolver;
    private Node node;

    private boolean development;
    private boolean stage;
    private boolean production;

    private String siteLanguage;

    private String data;

    @PostConstruct
    public void initModel() {
        createErrorTypesMap();

        resource = request.getResource();
        resourceResolver = resource.getResourceResolver();
        try {
            node = resource.adaptTo(Node.class);
            updateRunMode();
            buildAnalyticsData();
        } catch (RepositoryException re) {
            LOGGER.error("Error during initialization Adobe Analytics = {}", re.getMessage(), re);
        }
    }

    private void createErrorTypesMap() {
        errorTypesMap = new HashMap<>();
        errorTypesMap.put(String.valueOf(HttpServletResponse.SC_NOT_FOUND), "resource not found");
        errorTypesMap.put(String.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR), "internal server error");
    }

    private void updateRunMode() {
        if (slingSettingsService != null) {
            final Set<String> runModes = slingSettingsService.getRunModes();
            if (runModes.contains(PROD_RUN_MODE)) {
                production = true;
            } else if (runModes.contains(STAGE_RUN_MODE)) {
                stage = true;
            } else if (runModes.contains(DEV_RUN_MODE) || runModes.contains(QA_RUN_MODE)) {
                development = true;
            }
        }
    }

    private String getTemplatePath() throws RepositoryException {
        if (node.hasProperty(NameConstants.NN_TEMPLATE)) {
            return node.getProperty(NameConstants.NN_TEMPLATE).getString();
        }
        return StringUtils.EMPTY;
    }

    private boolean isErrorPage() throws RepositoryException {
        return getTemplatePath().contains(ERROR_PAGE_TEMPLATE_NAME);
    }

    private List<String> generateSiteSections() {
        String sectionPath = Text.getAbsoluteParent(resource.getPath(), TLConstants.SOLUTIONS_SECTION_MENU_PAGE_LEVEL);
        String path = request.getPathInfo().replace(sectionPath, StringUtils.EMPTY).replace(".html", StringUtils.EMPTY);
        return Arrays.stream(path.split(TLConstants.SLASH)).filter(s -> !s.equals(StringUtils.EMPTY)).collect(Collectors.toList());
    }

    public boolean isDevelopment() {
        return development;
    }

    public boolean isStage() {
        return stage;
    }

    public boolean isProduction() {
        return production;
    }

    private String setChannel() throws RepositoryException {
        if (isErrorPage()) {
            return ERROR_VALUE;
        }
        String sectionPath = Text.getAbsoluteParent(resource.getPath(), TLConstants.CHAPTER_LEVEL);
        return resourceResolver.resolve(sectionPath).getName();
    }

    private String setPageName(String channel, String errorCode) throws RepositoryException {
        siteLanguage = StringUtils.EMPTY;
        String pageName = null;
        if (isErrorPage()) {
            pageName = String.join(COLON, new String[]{errorCode, ERROR_VALUE});
        } else {
            pageName = generateSiteSections().stream().collect(Collectors.joining(COLON));
        }

        Page currentPage = PageUtil.getCurrentPage(resource);
        if (currentPage != null) {
            Page languagePage = currentPage.getAbsoluteParent(TLConstants.LANGUAGE_PAGE_LEVEL);
            if (languagePage != null) {
                siteLanguage = languagePage.getName();
            }
        }
        return String.join(COLON, new String[]{"tl", siteLanguage, channel, pageName});
    }

    private String setPageType() throws RepositoryException {
        if (isErrorPage()) {
            return ERROR_PAGE_TYPE_VALUE;
        }
        Resource templateResource = resourceResolver.resolve(getTemplatePath());
        return templateResource.getName();
    }

    private String[] setSiteSections() throws RepositoryException {
        if (isErrorPage()) {
            return new String[]{ERROR_VALUE};
        }
        List<String> sections = generateSiteSections();
        String[] array = new String[sections.size()];
        return sections.toArray(array);
    }

    private String setErrorCode() throws RepositoryException {
        if (isErrorPage()) {
            for (String errorCode : ERROR_CODES_ARRAY) {
                if (resource.getPath().contains(errorCode)) {
                    return errorCode;
                }
            }
        }
        return StringUtils.EMPTY;
    }

    private String setErrorType() throws RepositoryException {
        if (isErrorPage()) {
            for (String errorCode : ERROR_CODES_ARRAY) {
                if (resource.getPath().contains(errorCode)) {
                    return errorTypesMap.getOrDefault(errorCode, StringUtils.EMPTY);
                }
            }
        }
        return StringUtils.EMPTY;
    }

    private void buildAnalyticsData() throws RepositoryException {
        String channel = setChannel();
        String[] sections = setSiteSections();
        String errorCode = setErrorCode();

        JsonObject jsonData = new JsonObject();
        JsonObject pageInfo = new JsonObject();
        pageInfo.addProperty("channel", channel);
        pageInfo.addProperty("pageName", setPageName(channel, errorCode));
        pageInfo.addProperty("pageType", setPageType());

        for (int i = 0; i < sections.length; i++) {
            pageInfo.addProperty(String.format("siteSection%s", i + 1), sections[i]);
        }
        pageInfo.addProperty("siteCountry", siteLanguage);
        pageInfo.addProperty("siteLanguage", siteLanguage);
        pageInfo.addProperty("siteName", TLConstants.SITE_NAME);
        pageInfo.addProperty("event", CONTENT_LOAD_EVENT);

        JsonObject userInfo = new JsonObject();
        userInfo.addProperty("userId", StringUtils.EMPTY);
        userInfo.addProperty("userRole", StringUtils.EMPTY);
        userInfo.addProperty("logInStatus", LOGIN_STATUS);
        userInfo.addProperty("userLanguage", StringUtils.EMPTY);
        userInfo.addProperty("userType", StringUtils.EMPTY);

        JsonObject errorData = new JsonObject();
        errorData.addProperty("errorcode", errorCode);
        errorData.addProperty("errortype", setErrorType());

        jsonData.add("pageinfo", pageInfo);
        jsonData.add("userinfo", userInfo);
        jsonData.add(ERROR_VALUE, errorData);

        final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
        this.data = gson.toJson(jsonData);
    }

    public String getData() {
        return this.data;
    }
}
