package com.tetralaval.models.search;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.tagging.TagConstants;
import com.day.cq.wcm.api.Page;
import com.tetralaval.constants.TLConstants;
import com.tetralaval.services.ArticleService;
import com.tetralaval.services.SearchResultsService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
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

    @SlingObject
    private SlingHttpServletRequest request;

    @Inject
    private Page page;

    @Inject
    private ArticleService articleService;

    @Inject
    private SearchResultsService searchResultsService;

    @ValueMapValue
    @Named(value = TagConstants.PN_TAGS)
    private String[] tags;

    @ValueMapValue
    private String mediaLabel;

    @ValueMapValue
    private String assetsPath;

    private String servletPath;
    private List<FilterModel> contentTypeList;
    private List<FilterModel> themeList;

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

    private List<FilterModel> getTags() {
        return searchResultsService.getFilters(request, tags);
    }

    public String getServletPath() {
        return servletPath;
    }

    public List<FilterModel> getContentTypeList() {
        return contentTypeList;
    }

    public List<FilterModel> getThemeList() {
        return themeList;
    }
}