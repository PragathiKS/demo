package com.tetrapak.publicweb.core.models;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.publicweb.core.beans.SearchResultBean;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings("deprecation")
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SearchResultsModel {

    @Self
    Resource resource;

    @Inject
    private String searchBoxPlaceholder;

    @Inject
    private String resultsText;

    @Inject
    private String noFilterMatches;

    @Inject
    private String noResultsText;

    @Inject
    private String firstTabLinkText;

    @Inject
    private String resultsPerPage;

    @Inject
    private String[] tabs;

    @Inject
    private String filterTitle;

    @Inject
    private String[] filterTagPaths;

    private String templateBasePath = "/conf/publicweb/settings/wcm/templates/";

    protected final Logger log = LoggerFactory.getLogger(SearchResultsModel.class);
    private LinkedHashMap<String, String> tagsMap = new LinkedHashMap<>();

    @PostConstruct
    protected void init() {
        log.info("Executing init method.");
        ResourceResolver resourceResolver = resource.getResourceResolver();
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        TagManager tagManager = resourceResolver.adaptTo(TagManager.class);

        if (pageManager != null && tagManager != null) {
            Page currentPage = pageManager.getContainingPage(resource);
            Locale locale = currentPage.getLanguage(true);
            if (filterTagPaths != null) {
                for (String tagPath : filterTagPaths) {
                    log.info("Tag path : {}", tagPath);
                    Tag tag = tagManager.resolve(tagPath);
                    tagsMap.put(tag.getTitle(locale), tagPath);
                }
            }
        }

    }

    /**
     * Method to get the tab details from the multifield property saved in CRX for
     * each of the tab.
     *
     * @param tabs String[]
     * @return List<SearchResultBean>
     */
    public List<SearchResultBean> getTabDetails(String[] tabs) {
        List<SearchResultBean> tabList = new ArrayList<>();
        JSONObject jObj;
        try {
            if (tabs == null) {
                log.error("Tabs value is NULL");
            } else {
                for (int i = 0; i < tabs.length; i++) {
                    SearchResultBean bean = new SearchResultBean();
                    jObj = new JSONObject(tabs[i]);

                    if (jObj.has("tabTitle")) {
                        bean.setTitle(jObj.getString("tabTitle"));
                    }

                    if (jObj.has("pageType")) {
                        String pageType = jObj.getString("pageType");
                        log.info("Page Template Path : {}", pageType);
                        pageType = pageType.replace(templateBasePath, "");
                        bean.setProductType(pageType);

                    }
                    tabList.add(bean);
                }
            }
        } catch (Exception e) {
            log.error("Exception while Multi-field data {}", e.getMessage(), e);
        }
        return tabList;
    }

    public String getSearchBoxPlaceholder() {
        return searchBoxPlaceholder;
    }

    public String getResultsText() {
        return resultsText;
    }

    public String getNoResultsText() {
        return noResultsText;
    }

    public String getNoFilterMatches() {
        return noFilterMatches;
    }

    public String getResultsPerPage() {
        return resultsPerPage;
    }

    public String getFirstTabLinkText() {
        return firstTabLinkText;
    }

    public List<SearchResultBean> getTabs() {
        return getTabDetails(tabs);
    }

    public String getFilterTitle() {
        return filterTitle;
    }

    public Map<String, String> getTagsMap() {
        return tagsMap;
    }

}
