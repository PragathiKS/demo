package com.tetrapak.publicweb.core.services.impl;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.tetrapak.publicweb.core.beans.BestPracticeLineBean;
import com.tetrapak.publicweb.core.services.BestPracticeLineService;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
@Designate(ocd = BestPracticeLineServiceImpl.Config.class)
public class BestPracticeLineServiceImpl implements BestPracticeLineService {

    @ObjectClassDefinition(name = "Tetra Pak - Public Web Best Practice Line Service", description = "Tetra Pak - Public Web Best Practice Line Service")
    public static @interface Config {

        @AttributeDefinition(name = "Best Practice Line Page Template Path", description = "Path for the Best Practice Line Page template.")
        String bestpracticeTemplate() default "/apps/publicweb/templates/bestpracticelinepage";
    }

    private static final Logger log = LoggerFactory.getLogger(BestPracticeLineServiceImpl.class);

    private String bestPracticeTemplate;

    /**
     * This method is used to check whether a best practice line page is
     * linked with the given sub category and product type.
     *
     * @return Boolean
     */
    public Boolean checkIfPracticeLineExists(ResourceResolver resourceResolver, String productType, String subCategoryVal, String rootPath) {
        log.info("Executing checkIfPracticeLineExists method.");
        SearchResult result = executeQuery(resourceResolver, productType, subCategoryVal, rootPath);
        long totalHits = result.getTotalMatches();
        return (totalHits > 0);

    }

    /**
     * This method is used to execute the query to get the best practice
     * line page results based on the product type and sub category value.
     *
     * @param resourceResolver ResourceResolver
     * @param productType      String
     * @param subCategoryVal   String
     * @param rootPath         String
     * @return SearchResult
     */
    private SearchResult executeQuery(ResourceResolver resourceResolver, String productType, String subCategoryVal, String rootPath) {
        log.info("Executing executeQuery method.");
        Map<String, String> map = new HashMap<>();

        // adapt a ResourceResolver to a QueryBuilder
        QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
        Session session = resourceResolver.adaptTo(Session.class);

        // Adding query parameters
        map.put("path", rootPath);
        map.put("type", "cq:Page");

        // Page type is Practice Line page.
        map.put("1_property", "jcr:content/cq:template");
        map.put("1_property.value", bestPracticeTemplate);

        // Parameter to look for product type as a tag on the page.
        map.put("2_group.1_group.property", "jcr:content/cq:tags");
        map.put("2_group.1_group.property.value", productType);

        // Parameter to look for sub category value as a tag on the page.
        map.put("2_group.2_group.property", "jcr:content/cq:tags");
        map.put("2_group.2_group.property.value", subCategoryVal);

        map.put("2_group.p.and", "true");
        map.put("p.limit", "1");

        log.info("Here is the query PredicateGroup : {} ", PredicateGroup.create(map));
        Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);

        return query.getResult();
    }

    /**
     * This method is used to get the list of best practice lines pages by
     * executing the query based on a product type and sub category.
     */
    public List<BestPracticeLineBean> getListOfPracticeLines(ResourceResolver resourceResolver, String productType, String subCategoryVal, String rootPath) {
        log.info("Executing getListOfPracticeLines method.");
        SearchResult result = executeQuery(resourceResolver, productType, subCategoryVal, rootPath);

        // paging metadata
        List<BestPracticeLineBean> resources = new LinkedList<>();
        if (result.getHits().isEmpty())
            return resources;

        // add all the items to the result list
        for (Hit hit : result.getHits()) {
            BestPracticeLineBean practiceItem = getBestPracticeLineBean(resourceResolver, hit);
            resources.add(practiceItem);
        }

        return resources;
    }

    private BestPracticeLineBean getBestPracticeLineBean(ResourceResolver resourceResolver, Hit hit) {
        BestPracticeLineBean practiceItem = new BestPracticeLineBean();
        try {
            log.info("Hit : {}", hit.getPath());
            Resource res = resourceResolver.getResource(hit.getPath() + "/jcr:content");
            if (res != null) {
                ValueMap properties = res.adaptTo(ValueMap.class);
                practiceItem.setPracticeTitle(properties.get("title", String.class) != null ? properties.get("title", String.class) : properties.get("jcr:title", String.class));
                practiceItem.setVanityDescription(properties.get("vanityDescription", String.class) != null ? properties.get("vanityDescription", String.class) : "");
                practiceItem.setPracticeImagePath(properties.get("practiceImagePath", String.class) != null ? properties.get("practiceImagePath", String.class) : "");
                practiceItem.setPracticeImageAltI18n(properties.get("practiceImageAltI18n", String.class) != null ? properties.get("practiceImageAltI18n", String.class) : "");
                practiceItem.setCtaTexti18nKey(properties.get("ctaTexti18nKey", String.class) != null ? properties.get("ctaTexti18nKey", String.class) : "");
                practiceItem.setPracticePath(LinkUtils.sanitizeLink(hit.getPath()));
            }

        } catch (RepositoryException e) {
            log.error("There was an issue getting the resource {}", hit.toString());
        }
        return practiceItem;
    }

    @Activate
    protected void activate(final Config config) {
        this.bestPracticeTemplate = (String.valueOf(config.bestpracticeTemplate()) != null) ? String.valueOf(config.bestpracticeTemplate())
                : null;
        log.info("configure: BESTPRACTICE_TEMPLATE='{}'", this.bestPracticeTemplate);
    }

}
