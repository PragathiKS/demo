package com.tetralaval.models.search;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.tagging.TagConstants;
import com.day.cq.wcm.api.Page;
import com.tetralaval.services.ArticleService;
import com.tetralaval.services.SearchResultsService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * The Class SearchResultsModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SearchResultsModel {

    /** request */
    @SlingObject
    private SlingHttpServletRequest request;

    /** page */
    @Inject
    private Page page;

    /** articleService */
    @Inject
    private ArticleService articleService;

    /** searchResultsService */
    @Inject
    private SearchResultsService searchResultsService;

    /** tags */
    @ValueMapValue
    @Named(value = TagConstants.PN_TAGS)
    private String[] tags;

    /** mediaLabel */
    @ValueMapValue
    private String mediaLabel;

    /** assetsPath */
    @ValueMapValue
    private String assetsPath;

    /** servletPath */
    private String servletPath;
    /** contentTypeList */
    private List<FilterModel> contentTypeList;
    /** themeList */
    private List<FilterModel> themeList;

    /**
     * init method
     */
    @PostConstruct
    protected void init() {
        List<FilterModel> filterModel = articleService.getFilterTypes();
        if (mediaLabel != null) {
            filterModel.add(new FilterModel(searchResultsService.setMediaId(mediaLabel), mediaLabel));
        }

        this.servletPath = String.format("%s/%s", page.getPath(), JcrConstants.JCR_CONTENT);
        this.contentTypeList = filterModel;
        this.themeList = getTags();
    }

    /**
     * Get tags
     * @return list of FilterModel
     */
    private List<FilterModel> getTags() {
        return searchResultsService.getFilters(request, tags);
    }

    /**
     * servletPath getter
     * @return servletPath
     */
    public String getServletPath() {
        return servletPath;
    }

    /**
     * contentTypeList getter
     * @return contentTypeList
     */
    public List<FilterModel> getContentTypeList() {
        return contentTypeList;
    }

    /**
     * themeList getter
     * @return themeList
     */
    public List<FilterModel> getThemeList() {
        return themeList;
    }
}
