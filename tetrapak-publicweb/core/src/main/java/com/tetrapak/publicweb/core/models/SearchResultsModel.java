package com.tetrapak.publicweb.core.models;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @Self
    private Resource resource;

    /** The template map. */
    private Map<String, List<SearchPathModel>> templateMap = new LinkedHashMap<>();

    /** The template map. */
    private Map<String, List<SearchPathModel>> structureMap = new LinkedHashMap<>();
    /** The theme list. */
    private List<ThemeModel> themeList;

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
                templateMap = configurationModel.getTemplateMap();
                structureMap = configurationModel.getStructureMap();
                themeList = configurationModel.getThemeList();
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

}
