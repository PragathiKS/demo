package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.publicweb.core.models.multifield.TabBeanModel;
import com.tetrapak.publicweb.core.models.multifield.TabBeanSemiAutoModel;
import com.tetrapak.publicweb.core.utils.PageUtil;

/**
 * Model class for Tab list component.
 *
 * @author Sandip Kumar
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TabsListModel {

    @Self
    private Resource resource;

    /** The heading. */
    @ValueMapValue
    private String heading;

    /** The content Type. */
    @ValueMapValue
    private String contentType;

    /** The read More Text I18n. */
    @ValueMapValue
    private String readMoreTextI18n;

    /** The read More Path. */
    @ValueMapValue
    private String readMorePath;

    /** The read More Target. */
    @ValueMapValue
    private String readMoreTarget;

    /** The tags. */
    @ValueMapValue
    private String[] tags;

    /** The max teasers. */
    @Default(intValues = 9)
    @ValueMapValue
    private int maxTabs;

    /** The pw theme. */
    @ValueMapValue
    private String pwTheme;

    /** The pw padding. */
    @ValueMapValue
    private String pwPadding;

    private String tabType;

    /** The pw display. */
    @ValueMapValue
    private String pwDisplay;

    /** The anchor id. */
    @ValueMapValue
    private String anchorId;

    /** The anchor title. */
    @ValueMapValue
    private String anchorTitle;

    @Inject
    @Named(value = "tabsManual")
    private List<TabBeanModel> tabListManual = new ArrayList<>();

    @ValueMapValue
    @Named(value = "tabsSemi")
    private List<TabBeanSemiAutoModel> tabListSemiAuto = new ArrayList<>();

    private List<TabBeanModel> tabs = new ArrayList<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(TabsListModel.class);

    /**
     * Post construct method to get the tab content from the multifield property
     * saved in CRX for each of the tab.
     */
    @PostConstruct
    protected void init() {
	ResourceResolver resolver = resource.getResourceResolver();
	PageManager pageManager = resolver.adaptTo(PageManager.class);

	if ("automatic".equals(contentType) && pageManager != null) {
	    tabType = "imageText";
	    generateListAutomaticWay(resolver, pageManager);
	} else if ("semi-automatic".equals(contentType) && pageManager != null) {
	    tabType = "imageText";
	    generateListSemiAutomatically(pageManager);
	} else if ("manual".equals(contentType) && pageManager != null) {
	    generateListManually();
	}

    }

    /**
     * set list from tags
     * 
     * @param resolver,pageManager
     */
    private void generateListAutomaticWay(ResourceResolver resolver, PageManager pageManager) {
	SearchResult searchResults = executeQuery(resolver);
	for (Hit hit : searchResults.getHits()) {
	    try {
		setTabDatafromAggregator(pageManager, hit.getPath());
	    } catch (RepositoryException e) {
		LOGGER.info("RepositoryException in Tablist", e.getMessage(), e);
	    }
	}
    }

    /**
     * @param pageManager
     * @param pagePath
     */
    private void setTabDatafromAggregator(PageManager pageManager, String pagePath) {
	AggregatorModel aggregator = PageUtil.getAggregator(pageManager.getPage(pagePath));
	TabBeanModel tabBean = new TabBeanModel();
	tabBean.setTitle(aggregator.getTitle());
	tabBean.setDescription(aggregator.getDescription());
	tabBean.setFileRefrence(aggregator.getImagePath());
	tabBean.setAlt(aggregator.getAltText());
	tabBean.setLinkTexti18n(aggregator.getLinkText());
	tabBean.setLinkURL(aggregator.getLinkPath());
	tabBean.setTargetBlank(aggregator.getLinkTarget());
	tabBean.setPwLinkTheme(aggregator.getPwLinkTheme());
	tabBean.setPwButtonTheme(aggregator.getPwButtonTheme());
	tabs.add(tabBean);
    }

    /**
     * set list from paths
     * 
     * @param pageManager
     */
    private void generateListSemiAutomatically(PageManager pageManager) {
	for (TabBeanSemiAutoModel pages : tabListSemiAuto) {
	    setTabDatafromAggregator(pageManager, pages.getPageURL());
	}
    }

    /**
     * set list from manual authoring
     */
    private void generateListManually() {
	tabs.addAll(tabListManual);
    }

    private SearchResult executeQuery(ResourceResolver resourceResolver) {
	LOGGER.info("Executing executeQuery method.");
	Map<String, String> map = new HashMap<>();

	// adapt a ResourceResolver to a QueryBuilder
	QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
	Session session = resourceResolver.adaptTo(Session.class);

	// Adding query parameters
	map.put("path", PageUtil.getLanguagePage(resource).getPath());
	map.put("type", "cq:Page");

	// Parameter to look for tags on the page.
	if (tags != null && tags.length > 0) {
	    map.put("1_group.p.and", "true");
	    for (int i = 0; i < tags.length; i++) {
		map.put("1_group." + (i + 1) + "_group.property", "jcr:content/cq:tags");
		map.put("1_group." + (i + 1) + "_group.property.value", tags[i]);
	    }
	}
	map.put("orderby", "jcr:content/cq:lastModified");
	map.put("orderby.sort", "desc");
	map.put("p.limit", String.valueOf(maxTabs));

	LOGGER.info("Here is the query PredicateGroup : {} ", PredicateGroup.create(map));
	Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);

	return query.getResult();
    }

    public String getHeading() {
	return heading;
    }

    public String getReadMoreTextI18n() {
	return readMoreTextI18n;
    }

    public String getReadMorePath() {
	return readMorePath;
    }

    public String getReadMoreTarget() {
	return readMoreTarget;
    }

    public String getPwTheme() {
	return pwTheme;
    }

    public String getPwPadding() {
	return pwPadding;
    }

    public String getPwDisplay() {
	return pwDisplay;
    }

    public List<TabBeanModel> getTabs() {
	return tabs;
    }

    public String[] getTags() {
	return tags;
    }

    public String getTabType() {
	return tabType;
    }

    public String getAnchorId() {
	return anchorId;
    }

    public String getAnchorTitle() {
	return anchorTitle;
    }

}
