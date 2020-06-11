package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.models.multifield.SearchListModel;
import com.tetrapak.publicweb.core.utils.LinkUtils;

/**
 * The Class SearchResultsModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SearchFilterModel {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchFilterModel.class);

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The resource. */
    @Self
    private Resource resource;

    /** The content type list. */
    List<SearchListModel> contentTypeList = new ArrayList<>();

    /** The theme list. */
    List<SearchListModel> themeList = new ArrayList<>();

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
            SearchConfigModel configurationModel = searchConfigResource.adaptTo(SearchConfigModel.class);
            if (Objects.nonNull(configurationModel)) {
                themeList = configurationModel.getThemeList();
                setContentType(configurationModel.getEventLabel(), PWConstants.EVENTS);
                setContentType(configurationModel.getNewsLabel(), PWConstants.NEWS);
                setContentType(configurationModel.getProductLabel(), PWConstants.PRODUCTS);
                setContentType(configurationModel.getCaseLabel(), PWConstants.CASES);
                setContentType(configurationModel.getMediaLabel(), PWConstants.MEDIA);
            }
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
        return themeList;
    }
}
