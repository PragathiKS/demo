package com.tetrapak.publicweb.core.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.ProductPagesSearchService;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import com.tetrapak.publicweb.core.utils.PageUtil;

/**
 * Implementation class for {@link ProductPagesSearchService}
 *
 */
@Component(
        immediate = true,
        service = ProductPagesSearchService.class,
        configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class ProductPagesSearchServiceImpl implements ProductPagesSearchService {

    @Reference
    private QueryBuilder queryBuilder;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductPagesSearchServiceImpl.class);

    /**
     * Gets the product page map where key is product id and value is product page path.
     *
     * @param list
     * @param resource
     *
     * @return the product page map
     */
    @Override
    public Map<String, String> getProductPageMap(final List<String> list, final Resource resource) {
        final Map<String, String> productPageMap = new HashMap<>();
        final SearchResult searchResult = executeProductIdQuery(list, resource);
        for (final Hit hit : searchResult.getHits()) {
            try {
                LOGGER.debug("Product page path: {}", hit.getPath());
                final ResourceResolver resourceResolver = resource.getResourceResolver();
                final Resource pageResource = resourceResolver.getResource(hit.getPath());
                final ValueMap valueMap = pageResource.getValueMap();
                if (valueMap.containsKey(PWConstants.PRODUCT_ID)) {
                    final String pagePath = LinkUtils
                            .sanitizeLink(StringUtils.substringBeforeLast(hit.getPath(), PWConstants.SLASH));
                    LOGGER.debug("Product id: {} and page path: {}", valueMap.get(PWConstants.PRODUCT_ID), pagePath);
                    productPageMap.put(valueMap.get(PWConstants.PRODUCT_ID).toString(), pagePath);
                }
            } catch (final RepositoryException e) {
                LOGGER.error("Repository exception while searching for product id: ", e);
            }
        }
        return productPageMap;
    }

    /**
     * Execute product id query.
     *
     * @param list
     * @param resource
     *
     * @return the search result
     */
    private SearchResult executeProductIdQuery(final List<String> list, final Resource resource) {
        LOGGER.info("Inside executeProductIdQuery()");
        final ResourceResolver resourceResolver = resource.getResourceResolver();
        final Session session = resourceResolver.adaptTo(Session.class);
        final Query query = queryBuilder.createQuery(PredicateGroup.create(populateQueryMap(list, resource)), session);
        return query.getResult();
    }

    /**
     * Populate query map.
     *
     * @param idList
     * @param resource
     *
     * @return the map
     */
    private Map<String, String> populateQueryMap(final List<String> idList, final Resource resource) {
        LOGGER.debug("Id list size: {}", idList.size());
        final Map<String, String> map = new HashMap<>();
        map.put("path", PageUtil.getLanguagePage(resource).getPath());
        map.put("property", PWConstants.PRODUCT_ID);
        for (int i = 0; i < idList.size(); i++) {
            map.put("property." + (i + 1) + "_value", idList.get(i));
        }
        map.put("p.limit", "-1");
        return map;
    }

}
