package com.tetralaval.models.analytics;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagConstants;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.tetralaval.constants.TLConstants;
import com.tetralaval.utils.PageUtil;
import org.apache.commons.lang3.ArrayUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Adobe Analytics Model
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AdobeAnalyticsModel {
    /** LOGGER constant */
    private static final Logger LOGGER = LoggerFactory.getLogger(AdobeAnalyticsModel.class);

    /** DEV_RUN_MODE constant */
    private static final String DEV_RUN_MODE = "dev";
    /** QA_RUN_MODE constant */
    private static final String QA_RUN_MODE = "qa";
    /** STAGE_RUN_MODE constant */
    private static final String STAGE_RUN_MODE = "stage";
    /** PROD_RUN_MODE constant */
    private static final String PROD_RUN_MODE = "prod";
    /** SKIP_NUMBER constant */
    private static final long SKIP_NUMBER = 2;
    /** SITE_SECTIONS_LENGTH constant */
    private static final long SITE_SECTIONS_LENGTH = 6;
    /** CONTENT_LOAD_EVENT constant */
    private static final String CONTENT_LOAD_EVENT = "content-load";
    /** LOGIN_STATUS constant */
    private static final String LOGIN_STATUS = "logged-in";
    /** USER_LANGUAGE constant */
    private static final String USER_LANGUAGE = "en";
    /** USER_TYPE constant */
    private static final String USER_TYPE = "customer";
    /** ERROR_PAGE_TEMPLATE_NAME constant */
    private static final String ERROR_PAGE_TEMPLATE_NAME = "error-page-template";
    /** ERROR_VALUE constant */
    private static final String ERROR_VALUE = "error";
    /** ERROR_PAGE_TYPE_VALUE constant */
    private static final String ERROR_PAGE_TYPE_VALUE = "errorPage";
    /** ERROR_CODES_ARRAY constant */
    private static final String[] ERROR_CODES_ARRAY = new String[]{
            String.valueOf(HttpServletResponse.SC_NOT_FOUND),
            String.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    };

    /** SlingHttpServletRequest */
    @SlingObject
    private SlingHttpServletRequest request;

    /** SlingSettingsService */
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

    /**
     * Method which generate adobe analytics data
     */
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

    /**
     * Method which set map of error types
     */
    private void createErrorTypesMap() {
        errorTypesMap = new HashMap<>();
        errorTypesMap.put(String.valueOf(HttpServletResponse.SC_NOT_FOUND), "resource not found");
        errorTypesMap.put(String.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR), "internal server error");
    }

    /**
     * Set run mode/environment
     */
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

    /**
     * Return template path
     *
     * @return template path
     * @throws RepositoryException
     */
    private String getTemplatePath() throws RepositoryException {
        if (node.hasProperty(NameConstants.NN_TEMPLATE)) {
            return node.getProperty(NameConstants.NN_TEMPLATE).getString();
        }
        return StringUtils.EMPTY;
    }

    /**
     * Check if page is an error page
     * @return
     * @throws RepositoryException
     */
    private boolean isErrorPage() throws RepositoryException {
        return getTemplatePath().contains(ERROR_PAGE_TEMPLATE_NAME);
    }

    /**
     * Generate site sections for analytics
     * @return list of site sections
     */
    private List<String> generateSiteSections() {
        String path = request.getPathInfo().replace(TLConstants.ROOT_PATH, StringUtils.EMPTY)
                .replace(TLConstants.HTML_EXTENSION, StringUtils.EMPTY);
        return Arrays.stream(path.split(TLConstants.SLASH)).filter(s -> !s.equals(StringUtils.EMPTY))
                .skip(SKIP_NUMBER).collect(Collectors.toList());
    }

    /**
     * development getter
     * @return development
     */
    public boolean isDevelopment() {
        return development;
    }

    /**
     * stage getter
     * @return stage
     */
    public boolean isStage() {
        return stage;
    }

    /**
     * production getter
     * @return production
     */
    public boolean isProduction() {
        return production;
    }

    /**
     * Set channel
     * @return channel value
     * @throws RepositoryException
     */
    private String setChannel() throws RepositoryException {
        if (isErrorPage()) {
            return ERROR_VALUE;
        }
        String sectionPath = Text.getAbsoluteParent(resource.getPath(), TLConstants.CHAPTER_LEVEL);
        return resourceResolver.resolve(sectionPath).getName();
    }

    /**
     * Set page name
     * @param errorCode
     * @return page name
     * @throws RepositoryException
     */
    private String setPageName(String errorCode) throws RepositoryException {
        siteLanguage = StringUtils.EMPTY;
        String pageName = null;
        if (isErrorPage()) {
            pageName = String.join(TLConstants.COLON, errorCode, ERROR_VALUE);
        } else {
            pageName = generateSiteSections().stream().collect(Collectors.joining(TLConstants.COLON));
        }

        Page currentPage = PageUtil.getCurrentPage(resource);
        if (currentPage != null) {
            Page languagePage = currentPage.getAbsoluteParent(TLConstants.LANGUAGE_PAGE_LEVEL);
            if (languagePage != null) {
                siteLanguage = languagePage.getName();
            }
        }
        return String.join(TLConstants.COLON, "tl", siteLanguage, pageName);
    }

    /**
     * Set page type
     * @return page type
     * @throws RepositoryException
     */
    private String setPageType() throws RepositoryException {
        if (isErrorPage()) {
            return ERROR_PAGE_TYPE_VALUE;
        }
        Resource templateResource = resourceResolver.resolve(getTemplatePath());
        return templateResource.getName();
    }

    /**
     * Set page categories
     * @return page categories
     */
    private String setPageCategories() {
        TagManager tagManager = request.getResource().getResourceResolver().adaptTo(TagManager.class);
        Page currentPage = PageUtil.getCurrentPage(resource);
        if (currentPage != null) {
            List<String> tagsList = new ArrayList<>();

            final String[] tagValue = currentPage.getProperties().get(TagConstants.PN_TAGS, String[].class);
            if (ArrayUtils.isNotEmpty(tagValue)) {
                for (String tags : tagValue) {
                    Tag tag = tagManager.resolve(tags);
                    tagsList.add(tag.getTitle());
                }

                String[] tagsArray = new String[tagsList.size()];
                return String.join(TLConstants.COMA, tagsList.toArray(tagsArray));
            }
        }


        return StringUtils.EMPTY;
    }

    /**
     * Set site sections
     * @return list of site sections
     * @throws RepositoryException
     */
    private String[] setSiteSections() throws RepositoryException {
        if (isErrorPage()) {
            return new String[]{ERROR_VALUE};
        }
        List<String> sections = generateSiteSections();
        if (!sections.isEmpty()) {
            List<String> subsections = sections.subList(1, sections.size());
            String[] array = new String[subsections.size()];
            return subsections.toArray(array);
        }
        return new String[]{};
    }

    /**
     * Set error code
     * @return error code
     * @throws RepositoryException
     */
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

    /**
     * Set error type
     * @return error type
     * @throws RepositoryException
     */
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

    /**
     * Build analytics data
     * @throws RepositoryException
     */
    private void buildAnalyticsData() throws RepositoryException {
        String[] sections = setSiteSections();
        String errorCode = setErrorCode();

        JsonObject jsonData = new JsonObject();
        JsonObject pageInfo = new JsonObject();
        pageInfo.addProperty("channel", setChannel());
        pageInfo.addProperty("pageName", setPageName(errorCode));
        pageInfo.addProperty("pageType", setPageType());
        pageInfo.addProperty("pageCategories", setPageCategories());

        for (int i = 0; i < SITE_SECTIONS_LENGTH; i++) {
            String value = StringUtils.EMPTY;
            if (i < sections.length) {
                value = sections[i];
            }
            pageInfo.addProperty(String.format("siteSection%s", i + 1), value);
        }
        pageInfo.addProperty("siteCountry", siteLanguage);
        pageInfo.addProperty("siteLanguage", siteLanguage);
        pageInfo.addProperty("siteName", TLConstants.SITE_NAME);
        pageInfo.addProperty("event", CONTENT_LOAD_EVENT);

        JsonObject userInfo = new JsonObject();
        userInfo.addProperty("userId", StringUtils.EMPTY);
        userInfo.addProperty("userRole", StringUtils.EMPTY);
        userInfo.addProperty("logInStatus", LOGIN_STATUS);
        userInfo.addProperty("userLanguage", USER_LANGUAGE);
        userInfo.addProperty("userType", USER_TYPE);

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

    /**
     * data getter
     * @return data
     */
    public String getData() {
        return this.data;
    }
}
