package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.models.multifield.SearchListModel;
import com.tetrapak.publicweb.core.models.multifield.SearchPathModel;
import com.tetrapak.publicweb.core.utils.LinkUtils;

/**
 * The Class SearchResultsModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SearchResultsModel {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchResultsModel.class);

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The current page. */
    @Inject
    private Page currentPage;

    /** The resource. */
    @Self
    private Resource resource;

    /** The theme map. */
    private Map<String, String> themeMap = new LinkedHashMap<>();

    /** The template map. */
    private Map<String, String> templateMap = new HashMap<>();

    /** The configuration model. */
    private SearchConfigModel configurationModel = new SearchConfigModel();
    
    /** The content type list. */
    private List<SearchListModel> contentTypeList = new ArrayList<>();
    
    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        LOGGER.debug("inside init method");
        final String rootPath = LinkUtils.getRootPath(request.getPathInfo());
        final String path = rootPath + "/jcr:content/root/responsivegrid/searchconfig";
        final Resource searchConfigResource = request.getResourceResolver().getResource(path);
        if (Objects.nonNull(searchConfigResource)) {
            configurationModel = searchConfigResource.adaptTo(SearchConfigModel.class);
            setContentType(configurationModel.getProductLabel(), PWConstants.PRODUCTS);
            setContentType(configurationModel.getNewsLabel(), PWConstants.NEWS);
            setContentType(configurationModel.getEventLabel(), PWConstants.EVENTS);
            setContentType(configurationModel.getCaseLabel(), PWConstants.CASES);
            setContentType(configurationModel.getMediaLabel(), PWConstants.MEDIA);
        }
    }
    
    /**
     * Sets the content type.
     *
     * @param label the label
     * @param key the key
     */
    private void setContentType(String label, String key) {
        if (StringUtils.isNoneBlank(label)) {
            SearchListModel contentType = new SearchListModel();
            contentType.setKey(key);
            contentType.setLabel(label);
            contentTypeList.add(contentType);
        }
    }

    /**
     * Gets the content type list.
     *
     * @return the content type list
     */
    public List<SearchListModel> getContentTypeList() {
        return contentTypeList;
    }

    /**
     * Gets the theme list.
     *
     * @return the theme list
     */
    public List<SearchListModel> getThemeList() {
        return configurationModel.getThemeList();
    }

    /**
     * Gets the product template list.
     *
     * @return the product template list
     */
    public List<SearchPathModel> getProductTemplateList() {
        return configurationModel.getProductTemplateList();
    }

    /**
     * Gets the product structure list.
     *
     * @return the product structure list
     */
    public List<SearchPathModel> getProductStructureList() {
        return configurationModel.getProductStructureList();
    }

    /**
     * Gets the news template list.
     *
     * @return the news template list
     */
    public List<SearchPathModel> getNewsTemplateList() {
        return configurationModel.getNewsTemplateList();
    }

    /**
     * Gets the news structure list.
     *
     * @return the news structure list
     */
    public List<SearchPathModel> getNewsStructureList() {
        return configurationModel.getNewsStructureList();
    }

    /**
     * Gets the event template list.
     *
     * @return the event template list
     */
    public List<SearchPathModel> getEventTemplateList() {
        return configurationModel.getEventTemplateList();
    }

    /**
     * Gets the event structure list.
     *
     * @return the event structure list
     */
    public List<SearchPathModel> getEventStructureList() {
        return configurationModel.getEventStructureList();
    }

    /**
     * Gets the case template list.
     *
     * @return the case template list
     */
    public List<SearchPathModel> getCaseTemplateList() {
        return configurationModel.getCaseTemplateList();
    }

    /**
     * Gets the case structure list.
     *
     * @return the case structure list
     */
    public List<SearchPathModel> getCaseStructureList() {
        return configurationModel.getCaseStructureList();
    }

    /**
     * Gets the media structure list.
     *
     * @return the media structure list
     */
    public List<SearchPathModel> getMediaStructureList() {
        return configurationModel.getMediaStructureList();
    }

    /**
     * Gets the gated content list.
     *
     * @return the gated content list
     */
    public List<SearchPathModel> getGatedContentList() {
        return configurationModel.getGatedContentList();
    }

    /**
     * Gets the servlet path.
     *
     * @return the servlet path
     */
    public String getServletPath() {
        return request.getResource().getPath();
    }

    /**
     * Gets the theme map.
     *
     * @return the theme map
     */
    public Map<String, String> getThemeMap() {
        if (Objects.nonNull(configurationModel) && getThemeList() != null && !getThemeList().isEmpty()) {
            for (SearchListModel theme : getThemeList()) {
                themeMap.put(theme.getKey(), theme.getTag());
            }
        }
        return themeMap;
    }

    /**
     * Gets the event label.
     *
     * @return the event label
     */
    /*
     * Gets the event label.
     *
     * @return the event label
     */
    public String getEventLabel() {
        return configurationModel.getEventLabel();
    }

    /**
     * Gets the case label.
     *
     * @return the case label
     */
    public String getCaseLabel() {
        return configurationModel.getCaseLabel();
    }

    /**
     * Gets the news label.
     *
     * @return the news label
     */
    public String getNewsLabel() {
        return configurationModel.getNewsLabel();
    }

    /**
     * Gets the product label.
     *
     * @return the product label
     */
    public String getProductLabel() {
        return configurationModel.getProductLabel();
    }

    /**
     * Gets the media label.
     *
     * @return the media label
     */
    public String getMediaLabel() {
        return configurationModel.getMediaLabel();
    }

    /**
     * Gets the video thumbnail.
     *
     * @return the video thumbnail
     */
    public String getVideoThumbnail() {
        return configurationModel.getVideoThumbnail();
    }

    /**
     * Gets the document thumbnail.
     *
     * @return the document thumbnail
     */
    public String getDocumentThumbnail() {
        return configurationModel.getDocumentThumbnail();
    }

    /**
     * Gets the template map.
     *
     * @return the template map
     */
    public Map<String, String> getTemplateMap() {
        setTemplateMap(getNewsTemplateList(), PWConstants.NEWS);
        setTemplateMap(getEventTemplateList(), PWConstants.EVENTS);
        setTemplateMap(getCaseTemplateList(), PWConstants.CASES);
        setTemplateMap(getProductTemplateList(), PWConstants.PRODUCTS);
        return templateMap;
    }

    /**
     * Sets the template map.
     *
     * @param list the list
     * @param key the key
     */
    private void setTemplateMap(List<SearchPathModel> list, String key) {
        if (list != null && !list.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (SearchPathModel pathObject : list) {
                sb.append(pathObject.getPath());
                sb.append(",");
            }
            templateMap.put(key, sb.toString().substring(0, sb.length() - 1));
        }
    }

    /**
     * Gets the current page.
     *
     * @return the current page
     */
    public Page getCurrentPage() {
        return currentPage;
    }

}