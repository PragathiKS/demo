package com.tetrapak.publicweb.core.services.impl;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.tetrapak.publicweb.core.services.TeaserSearchService;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.*;

@Component(immediate = true, service = TeaserSearchService.class)
public class TeaserSearchServiceImpl implements TeaserSearchService {

    private static final Logger LOG = LoggerFactory.getLogger(TeaserSearchServiceImpl.class);

    @Override
    public List<Page> getListOfTeasers(ResourceResolver resourceResolver, String[] tags, String rootPath, int limit) {
        if(null == tags || tags.length<1){
            return Collections.emptyList();
        }
        SearchResult searchResult = executeQuery(resourceResolver, tags, rootPath, limit);
        if (null == searchResult || searchResult.getHits().isEmpty()) {
            return Collections.emptyList();
        }
        return getListOfTeasers(searchResult);
    }

    /**
     * This method is used to execute the query to get the teaser list
     * results based on tags
     *
     * @param resourceResolver ResourceResolver
     * @param tags             tags
     * @param rootPath         String
     * @param limit            limit
     * @return SearchResult
     */
    private SearchResult executeQuery(ResourceResolver resourceResolver, String[] tags, String rootPath, int limit) {
        LOG.info("Executing executeQuery method.");
        Map<String, String> map = new HashMap<>();

        QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
        if (null != queryBuilder) {
            Session session = resourceResolver.adaptTo(Session.class);

            map.put("path", rootPath);
            map.put("type", "cq:Page");

            map.put("1_group.1_property.property", "jcr:content/cq:tags");
            int i = 1;
            for (String tag : tags) {
                map.put("1_group." + i++ + "_property.value", tag);
            }
            map.put("1_group.p.or", "true");

            map.put("p.limit", Integer.toString(limit));

            map.put("orderby", "@jcr:content/cq:lastReplicated");
            map.put("orderby.sort", "desc");

            LOG.info("Here is the query PredicateGroup : {} ", PredicateGroup.create(map));
            Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);

            return query.getResult();
        }
        return null;
    }

    private List<Page> getListOfTeasers(SearchResult result) {
        List<Page> pages = new ArrayList<>();
        for (Hit hit : result.getHits()) {
            try {
                pages.add(hit.getResource().adaptTo(Page.class));
            } catch (RepositoryException e) {
                LOG.error("RepositoryException while adapting search result hits to pages", e);
            }
        }
        return pages;
    }
}
