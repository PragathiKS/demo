package com.tetrapak.publicweb.core.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.publicweb.core.models.AggregatorModel;
import com.tetrapak.publicweb.core.models.multifield.PagePathsBeanModel;
import com.tetrapak.publicweb.core.services.AggregatorService;
import com.tetrapak.publicweb.core.utils.PageUtil;

@Component(immediate = true, service = AggregatorService.class)
public class AggregatorServiceImpl implements AggregatorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AggregatorServiceImpl.class);

    /**
     * @param resource
     * @param tags
     * @param maxTabs
     * @return aggregatorList
     */
    @Override
    public List<AggregatorModel> getAggregatorList(Resource resource, String[] tags, int maxTabs) {
	List<AggregatorModel> aggregatorList = new ArrayList<>();
	ResourceResolver resolver = resource.getResourceResolver();
	PageManager pageManager = resolver.adaptTo(PageManager.class);
	SearchResult searchResults = executeAggregatorQuery(resource, tags, maxTabs);
	for (Hit hit : searchResults.getHits()) {
	    try {
		AggregatorModel aggregator = getAggregator(pageManager.getPage(hit.getPath()));
		if (aggregator != null) {
		    aggregatorList.add(aggregator);
		}
	    } catch (RepositoryException e) {
		LOGGER.info("RepositoryException in getAggregatorList", e.getMessage(), e);
	    }
	}
	return aggregatorList;
    }

    /**
     * @param resource
     * @param pagePaths
     * @param maxTabs
     * @return aggregatorList
     */
    @Override
    public List<AggregatorModel> getAggregatorList(Resource resource, List<PagePathsBeanModel> pagePaths, int maxTabs) {
	List<AggregatorModel> aggregatorList = new ArrayList<>();
	ResourceResolver resolver = resource.getResourceResolver();
	PageManager pageManager = resolver.adaptTo(PageManager.class);
	for (PagePathsBeanModel pagePath : pagePaths) {
	    AggregatorModel aggregator = getAggregator(pageManager.getPage(pagePath.getPageURL()));
	    if (aggregator != null) {
		aggregatorList.add(aggregator);
	    }
	}
	return aggregatorList;
    }

    /**
     * @param currentPage
     * @return aggregator
     */
    @Override
    public AggregatorModel getAggregator(Page currentPage) {
	return currentPage.getContentResource().adaptTo(AggregatorModel.class);
    }

    /**
     * @param resource
     * @param tags
     * @param maxTabs
     * @return query results
     */
    public static SearchResult executeAggregatorQuery(Resource resource, String[] tags, int maxTabs) {
	LOGGER.info("Executing executeQuery method.");
	ResourceResolver resourceResolver = resource.getResourceResolver();
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

}
