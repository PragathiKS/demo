package com.tetrapak.publicweb.core.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
import com.tetrapak.publicweb.core.models.multifield.SemiAutomaticModel;
import com.tetrapak.publicweb.core.services.AggregatorService;
import com.tetrapak.publicweb.core.utils.PageUtil;
import com.tetrapak.publicweb.core.constants.PWConstants;

@Component(immediate = true, service = AggregatorService.class)
public class AggregatorServiceImpl implements AggregatorService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AggregatorServiceImpl.class);

	/**
	 * @param resource
	 * @param tags
	 * @param maxTabs
	 * @param logicalOperator
	 * @return aggregatorList
	 */
	@Override
	public List<AggregatorModel> getAggregatorList(Resource resource, String[] tags, int maxTabs, String logicalOperator) {
		List<AggregatorModel> aggregatorList = new ArrayList<>();
		List<AggregatorModel> publishedDateList= new ArrayList<>();
		ResourceResolver resolver = resource.getResourceResolver();
		PageManager pageManager = resolver.adaptTo(PageManager.class);
		SearchResult searchResults = executeAggregatorQuery(resource, tags, maxTabs,logicalOperator);
		for (Hit hit : searchResults.getHits()) {
			try {
				AggregatorModel aggregator = getAggregator(pageManager.getPage(hit.getPath()));
				if (null != aggregator && Objects.nonNull(hit.getProperties().get("articleDate", String.class))) {
					aggregatorList.add(aggregator);
				} else if (null != aggregator && Objects.nonNull(hit.getProperties().get("cq:lastModified", String.class))) {
					publishedDateList.add(aggregator);
				}
			} catch (RepositoryException e) {
				LOGGER.info("RepositoryException in getAggregatorList", e.getMessage(), e);
			}
		}
		aggregatorList.addAll(publishedDateList);
		return aggregatorList;
	}

	/**
	 * @param resource
	 * @param pagePaths
	 * @param maxTabs
	 * @return aggregatorList
	 */
	@Override
	public List<AggregatorModel> getAggregatorList(Resource resource, List<SemiAutomaticModel> pagePaths) {
		List<AggregatorModel> aggregatorList = new ArrayList<>();
		ResourceResolver resolver = resource.getResourceResolver();
		PageManager pageManager = resolver.adaptTo(PageManager.class);
		for (SemiAutomaticModel pagePath : pagePaths) {
			// logic added to exclude external links or not available resources - SMAR-12038
			Page currentPage = pageManager.getPage(pagePath.getPageURL());
			if (currentPage != null) {
				Resource pageContentRes = currentPage.getContentResource();
				if (!(pageContentRes.getValueMap().containsKey(PWConstants.NOFOLLOW_PROPERTY) || pageContentRes.getValueMap().containsKey(PWConstants.NOINDEX_PROPERTY) || pageContentRes.getValueMap().containsKey(PWConstants.HIDEINSEARCH_PROPERTY))) {
					AggregatorModel aggregator = getAggregator(currentPage);
					if (aggregator != null) {
						aggregatorList.add(aggregator);
					}
				}
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
	 * Execute aggregator query.
	 *
	 * @param resource the resource
	 * @param tags the tags
	 * @param maxTabs the max tabs
	 * @param logicalOperator the logical operator
	 * @return query results
	 */
	public static SearchResult executeAggregatorQuery(Resource resource, String[] tags, int maxTabs, String logicalOperator) {
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
			map.put("1_group.p."+logicalOperator, "true");
			for (int i = 0; i < tags.length; i++) {
				map.put("1_group." + (i + 1) + "_group.property", "jcr:content/cq:tags");
				map.put("1_group." + (i + 1) + "_group.property.value", tags[i]);
			}
		}
		map.put("2_property","jcr:content/@noFollow");
		map.put("2_property.operation","not");
		map.put("3_property","jcr:content/@noIndex");
		map.put("3_property.operation","not");
		map.put("4_property","jcr:content/@hideInSearch");
		map.put("4_property.operation","not");
		map.put("104_orderby", "@jcr:content/articleDate");
		map.put("105_orderby", "@jcr:content/cq:lastModified");
		map.put("104_orderby.sort", "desc");
		map.put("105_orderby.sort", "desc");
		map.put("p.limit", String.valueOf(maxTabs));

		LOGGER.info("Here is the query PredicateGroup : {} ", PredicateGroup.create(map));
		Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);

		return query.getResult();
	}

}
