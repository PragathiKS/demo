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
    
    List<SearchListModel> contentTypeList = new ArrayList<>();
    
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
            if (Objects.nonNull(configurationModel)){
                themeList = configurationModel.getThemeList();
                SearchListModel contentType = new SearchListModel();
                if(StringUtils.isNoneBlank(configurationModel.getEventLabel())) {
                    contentType.setKey(PWConstants.EVENTS);
                    contentType.setLabel(configurationModel.getEventLabel());
                    contentTypeList.add(contentType);
                }
                if(StringUtils.isNoneBlank(configurationModel.getNewsLabel())) {
                    contentType.setKey(PWConstants.NEWS);
                    contentType.setLabel(configurationModel.getNewsLabel());
                    contentTypeList.add(contentType);
                }
                if(StringUtils.isNoneBlank(configurationModel.getProductLabel())) {
                    contentType.setKey(PWConstants.PRODUCTS);
                    contentType.setLabel(configurationModel.getProductLabel());
                    contentTypeList.add(contentType);
                }
                if(StringUtils.isNoneBlank(configurationModel.getCaseLabel())) {
                    contentType.setKey(PWConstants.CASES);
                    contentType.setLabel(configurationModel.getCaseLabel());
                    contentTypeList.add(contentType);
                }
                if(StringUtils.isNoneBlank(configurationModel.getMediaLabel())) {
                    contentType.setKey(PWConstants.MEDIA);
                    contentType.setLabel(configurationModel.getMediaLabel());
                    contentTypeList.add(contentType);
                }
             }
        }
    }

    public List<SearchListModel> getContentTypeList() {
        return contentTypeList;
    }

    public List<SearchListModel> getThemeList() {
        return themeList;
    }
}
