/*
 * 
 */
package com.tetrapak.publicweb.core.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.ReplicationStatus;
import com.day.cq.replication.Replicator;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.tetrapak.publicweb.core.constants.PWConstants;

/**
 * The Class DeltaFeedUtil.
 */
public final class DeltaFeedUtil {
    
    
    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DeltaFeedUtil.class);
    
    /**
     * Instantiates a new delta feed util.
     */
    private DeltaFeedUtil() {
        /*
         * adding a private constructor to hide the implicit one
         */
    }
    
    /**
     * replicate products.
     */
    public static void activateUpdatedProducts(ResourceResolver resolver, Replicator replicator, Session session,
            Iterable<String>  pathsToActivate) {
        try {
            for (String pathToActivate : pathsToActivate) {
                replicator.replicate(session, ReplicationActionType.ACTIVATE, pathToActivate);
                ResourceUtil.replicateChildResources(replicator, session, resolver.getResource(pathToActivate));
            }
        } catch (ReplicationException e) {
            LOGGER.error("Replication Exception in activating PXP products", e.getMessage(), e);
        }
    }
    
    /**
     * deactivate PDP's
     */
    public static void deactivatePDPs(ResourceResolver resolver, Replicator replicator, Session session,
            Set<String> productIds) {
        LOGGER.debug("'Deleted Product ID::" + productIds.toString());
        SearchResult result = executeQuery(resolver, productIds);
        if (result != null) {
            for (Hit hit : result.getHits()) {
                deactivatePDP(replicator, session, hit);
            }
        }
    }

    /**
     * @param resolver
     * @param replicator
     * @param session
     * @param hit
     */
    private static void deactivatePDP(Replicator replicator, Session session, Hit hit) {
        try {
            // Check if Page in Activation state then only deactivate PDP.
            ReplicationStatus status = replicator.getReplicationStatus(session, hit.getPath());
            if (status != null && status.isDelivered()) {
                ResourceUtil.deactivatePath(replicator, session, hit.getPath());
            }
        } catch (RepositoryException e) {
            LOGGER.error("RepositoryException Exception in deactivating PXP PDP", e.getMessage(), e);
        }
    }
    
    /**
     * Execute delta query.
     *
     * @param resourceResolver the resource resolver
     * @param productIds the product ids
     * @return query results
     */
    private static SearchResult executeQuery(ResourceResolver resolver, Set<String> productIds) {
        LOGGER.info("Executing executeQuery method.");
        if (productIds == null || productIds.isEmpty()) {
            return null;
        }
        // adapt a ResourceResolver to a QueryBuilder
        QueryBuilder queryBuilder = resolver.adaptTo(QueryBuilder.class);
        Session session = resolver.adaptTo(Session.class);

        // Adding query parameters
        Map<String, String> map = new HashMap<>();
        map.put("path", PWConstants.CONTENT_ROOT_PATH);
        map.put("type", "cq:Page");

        // Search product Id's on the page.

        map.put("2_group.p.or", "true");
        int i = 1;
        for (String productId : productIds) {
            map.put("2_group." + (i) + "_property", "jcr:content/productId");
            map.put("2_group." + (i) + "_property.value", productId);
            i++;
        }

        map.put("p.limit", "-1");

        LOGGER.info("Here is the query PredicateGroup : {} ", PredicateGroup.create(map));
        Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);

        return query.getResult();
    }

}
