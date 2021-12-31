package com.tetralaval.services.impl;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.tetralaval.services.ContentChangeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameterMap;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component(immediate = true, service = ContentChangeService.class)
public class ContentChangeServiceImpl implements ContentChangeService {
    /** LOGGER constant */
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentChangeServiceImpl.class);

    /** PATH_PARAM constant */
    private static final String PATH_PARAM = "path";
    /** FULLTEXT_PARAM constant */
    private static final String FULLTEXT_PARAM = "fulltext";
    /** OLD_VALUE_PARAM constant */
    private static final String OLD_VALUE_PARAM = "oldValue";
    /** NEW_VALUE_PARAM constant */
    private static final String NEW_VALUE_PARAM = "newValue";

    /** queryBuilder */
    @Reference
    private QueryBuilder queryBuilder;

    /**
     * Get results
     * @param request
     * @param params
     * @return map
     */
    @Override
    public Map<String, Object> getResults(SlingHttpServletRequest request, RequestParameterMap params) {
        ResourceResolver resourceResolver = request.getResourceResolver();
        Session session = resourceResolver.adaptTo(Session.class);

        Map<String, Object> map = new HashMap<>();
        List<String> paths = new ArrayList<>();

        Query query = queryBuilder.createQuery(PredicateGroup.create(getQueryParamMap(params)), session);
        SearchResult result = query.getResult();
        for (Hit hit : result.getHits()) {
            try {
                paths.add(updateContentPart(hit, params));
            } catch (RepositoryException re) {
                LOGGER.error("There was an issue getting the resource", re);
            }
        }
        try {
            session.save();

            paths = paths.stream().filter(path -> !path.isEmpty()).collect(Collectors.toList());
            map.put("paths", paths);
            map.put("updated", paths.size());
            map.put("size", result.getTotalMatches());
        } catch (RepositoryException re) {
            LOGGER.error("There was an issue saving the session", re);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
        return map;
    }

    private Map<String, String> getQueryParamMap(RequestParameterMap params) {
        Map<String, String> map = new HashMap<>();
        map.put(PATH_PARAM, String.valueOf(params.getValue(PATH_PARAM)));
        map.put(FULLTEXT_PARAM, String.valueOf(params.getValue(OLD_VALUE_PARAM)));
        map.put("p.limit", "-1");
        return map;
    }

    private String updateContentPart(Hit hit, RequestParameterMap params) throws RepositoryException {
        Node node = hit.getNode();
        PropertyIterator propertyIterator = node.getProperties();
        while (propertyIterator.hasNext()) {
            Property property = propertyIterator.nextProperty();
            if (!property.isMultiple()) {
                String value = property.getString();
                String oldValue = String.valueOf(params.getValue(OLD_VALUE_PARAM));
                String newValue = String.valueOf(params.getValue(NEW_VALUE_PARAM));
                if (value.indexOf(oldValue) != -1) {
                    node.setProperty(property.getName(), value.replace(oldValue, newValue));
                    return node.getPath();
                }
            }
        }
        return StringUtils.EMPTY;
    }
}
