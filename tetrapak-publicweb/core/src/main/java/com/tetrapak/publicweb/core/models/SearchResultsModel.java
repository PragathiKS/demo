package com.tetrapak.publicweb.core.models;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.tetrapak.publicweb.core.models.multifield.ContentTypeModel;
import com.tetrapak.publicweb.core.models.multifield.SearchPathModel;
import com.tetrapak.publicweb.core.models.multifield.ThemeModel;
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
    
    @Inject
    private Page currentPage;

    @Self
    private Resource resource;

    /** The template map. */
    private Map<String, List<SearchPathModel>> templateMap = new LinkedHashMap<>();

    /** The template map. */
    private Map<String, List<SearchPathModel>> structureMap = new LinkedHashMap<>();
    /** The theme list. */
    private List<ThemeModel> themeList;
    private List<ContentTypeModel> contentTypeList;
    private String mediaLabel;
    private String gatedPath;

    private Map<String, String> themeMap = new LinkedHashMap<>();

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
            final SearchConfigModel configurationModel = searchConfigResource.adaptTo(SearchConfigModel.class);
            if (Objects.nonNull(configurationModel)) {
                mediaLabel = configurationModel.getMediaLabel();
                gatedPath = configurationModel.getGatedPath();
                contentTypeList = configurationModel.getContentTypeList();
                for (ContentTypeModel contentTypeModel : contentTypeList) {
                    templateMap.put(contentTypeModel.getLabel(), contentTypeModel.getTemplateList());
                    structureMap.put(contentTypeModel.getLabel(), contentTypeModel.getStructureList());
                }
                themeList = configurationModel.getThemeList();
                themeMap = themeList.stream().collect(Collectors.toMap(ThemeModel::getThemeLabel, ThemeModel::getTag));
            }
        }
    }

    /**
     * Gets the template map.
     *
     * @return the template map
     */
    public Map<String, List<SearchPathModel>> getTemplateMap() {
        return templateMap;
    }

    public Map<String, List<SearchPathModel>> getStructureMap() {
        return structureMap;
    }

    /**
     * Gets the theme list.
     *
     * @return the theme list
     */
    public List<ThemeModel> getThemeList() {
        return themeList;
    }

    /**
     * Gets the servlet path.
     *
     * @return the servlet path
     */
    public String getServletPath() {
        return request.getResource().getPath();
    }

    public List<ContentTypeModel> getContentTypeList() {
        return contentTypeList;
    }

    public Map<String, String> getThemeMap() {
        return themeMap;
    }

    public Page getCurrentPage() {
        return currentPage;
    }
    
    public String getMediaLabel() {
        return mediaLabel;
    }

    public String getGatedPath() {
        return gatedPath;
    }

}
