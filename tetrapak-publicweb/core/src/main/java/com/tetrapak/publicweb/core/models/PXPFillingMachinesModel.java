package com.tetrapak.publicweb.core.models;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.tetrapak.publicweb.core.beans.pxp.FillingMachine;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import com.tetrapak.publicweb.core.utils.PageUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * The Class PXPFillingMachinesModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PXPFillingMachinesModel {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PXPFillingMachinesModel.class);

    /** The resource. */
    @Self
    private Resource resource;

    /** The resource resolver. */
    @Inject
    private ResourceResolver resourceResolver;

    /** The heading. */
    @ValueMapValue
    @Default(values = "Filling machine")
    private String heading;

    /** The pw theme. */
    @ValueMapValue
    private String pwTheme;

    /** The anchor id. */
    @ValueMapValue
    private String anchorId;

    /** The anchor title. */
    @ValueMapValue
    private String anchorTitle;

    /** The filling machine list. */
    private List<FillingMachine> fillingMachineList;

    /** The Constant PRODUCT_ID. */
    private static final String PRODUCT_ID = "productId";

    /**
     * The init method.
     */
    @PostConstruct
    protected void init() {
        final ProductModel product = resource.adaptTo(ProductModel.class);
        fillingMachineList = product.getFillingMachineReferences();
    }

    /**
     * Gets the filling machine list.
     *
     * @return the filling machine list
     */
    public List<FillingMachine> getFillingMachineList() {
        return new ArrayList<>(fillingMachineList);
    }

    /**
     * Gets the product page map where key is product id and value is product page path.
     *
     * @return the product page map
     */
    public Map<String, String> getProductPageMap() {
        final Map<String, String> productPageMap = new HashMap<>();
        final SearchResult searchResult = executeProductIdQuery();
        for (final Hit hit : searchResult.getHits()) {
            try {
                LOGGER.debug("Product page path: {}", hit.getPath());
                final Resource pageResource = resourceResolver.getResource(hit.getPath());
                final ValueMap valueMap = pageResource.getValueMap();
                if (valueMap.containsKey(PRODUCT_ID)) {
                    final String pagePath = LinkUtils.sanitizeLink(StringUtils.substringBeforeLast(hit.getPath(), "/"));
                    LOGGER.debug("Product id: {} and page path: {}", valueMap.get(PRODUCT_ID), pagePath);
                    productPageMap.put(valueMap.get(PRODUCT_ID).toString(), pagePath);
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
     * @return the search result
     */
    private SearchResult executeProductIdQuery() {
        LOGGER.info("Inside executeProductIdQuery()");
        final QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
        final Session session = resourceResolver.adaptTo(Session.class);
        final Query query = queryBuilder.createQuery(PredicateGroup.create(populateQueryMap()), session);
        return query.getResult();
    }

    /**
     * Populate query map.
     *
     * @return the map
     */
    private Map<String, String> populateQueryMap() {
        final List<String> idList = getIdList();
        LOGGER.debug("Id list size: {}", idList.size());
        final Map<String, String> map = new HashMap<>();
        map.put("path", PageUtil.getLanguagePage(resource).getPath());
        map.put("property", PRODUCT_ID);
        for (int i = 0; i < idList.size(); i++) {
            map.put("property." + (i + 1) + "_value", idList.get(i));
        }
        map.put("p.limit", "-1");
        return map;
    }

    /**
     * Gets the id list.
     *
     * @return the id list
     */
    private List<String> getIdList() {
        return fillingMachineList.stream().filter(list -> list.getId() != null).map(FillingMachine::getId)
                .collect(Collectors.toList());
    }

    /**
     * Gets the heading.
     *
     * @return the heading
     */
    public String getHeading() {
        return heading;
    }

    /**
     * Gets the pw theme.
     *
     * @return the pw theme
     */
    public String getPwTheme() {
        return pwTheme;
    }

    /**
     * Gets the anchor id.
     *
     * @return the anchor id
     */
    public String getAnchorId() {
        return anchorId;
    }

    /**
     * Gets the anchor title.
     *
     * @return the anchor title
     */
    public String getAnchorTitle() {
        return anchorTitle;
    }
}
