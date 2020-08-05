/*
 *
 */
package com.tetrapak.publicweb.core.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.jcr.RangeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;
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
import com.day.cq.wcm.api.WCMException;
import com.day.cq.wcm.msm.api.LiveRelationship;
import com.day.cq.wcm.msm.api.LiveRelationshipManager;
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
    public static void activateUpdatedProducts(final ResourceResolver resolver, final Replicator replicator,
            final Session session, final Iterable<String> pathsToActivate) {
        try {
            for (final String pathToActivate : pathsToActivate) {
                replicator.replicate(session, ReplicationActionType.ACTIVATE, pathToActivate);
                ResourceUtil.replicateChildResources(replicator, session, resolver.getResource(pathToActivate));
            }
        } catch (final ReplicationException e) {
            LOGGER.error("Replication Exception in activating PXP products {}  {}", e.getMessage(), e);
        }
    }

    /**
     * deactivate PDP's
     */
    public static void deactivatePDPs(final ResourceResolver resolver, final Replicator replicator,
            final Session session, final Set<String> productIds) {
        LOGGER.debug("'Deleted Product ID:: {}", productIds);
        final SearchResult result = executeQuery(resolver, productIds);
        if (result != null) {
            for (final Hit hit : result.getHits()) {
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
    private static void deactivatePDP(final Replicator replicator, final Session session, final Hit hit) {
        try {
            // Check if Page in Activation state then only deactivate PDP.
            final ReplicationStatus status = replicator.getReplicationStatus(session, hit.getPath());
            if (status != null && status.isDelivered()) {
                ResourceUtil.deactivatePath(replicator, session, hit.getPath());
            }
        } catch (final RepositoryException e) {
            LOGGER.error("RepositoryException Exception in deactivating PXP PDP {} {}", e.getMessage(), e);
        }
    }

    /**
     * Execute delta query.
     *
     * @param resourceResolver
     *            the resource resolver
     * @param productIds
     *            the product ids
     * @return query results
     */
    private static SearchResult executeQuery(final ResourceResolver resolver, final Set<String> productIds) {
        LOGGER.info("Executing executeQuery method.");
        if (productIds == null || productIds.isEmpty()) {
            return null;
        }
        // adapt a ResourceResolver to a QueryBuilder
        final QueryBuilder queryBuilder = resolver.adaptTo(QueryBuilder.class);
        final Session session = resolver.adaptTo(Session.class);

        // Adding query parameters
        final Map<String, String> map = new HashMap<>();
        map.put("path", PWConstants.CONTENT_ROOT_PATH);
        map.put("type", "cq:Page");

        // Search product Id's on the page.

        map.put("2_group.p.or", "true");
        int i = 1;
        for (final String productId : productIds) {
            map.put("2_group." + (i) + "_property", "jcr:content/productId");
            map.put("2_group." + (i) + "_property.value", productId);
            i++;
        }
        map.put("p.limit", "-1");
        LOGGER.info("Here is the query PredicateGroup : {} ", PredicateGroup.create(map));
        final Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);

        return query.getResult();
    }

    /**
     *
     * @param resolver
     * @param replicator
     * @param session
     * @param langsToActivate
     * @param liveRelManager
     */
    public static void cachePurge(final ResourceResolver resolver, final Replicator replicator, final Session session,
            final List<String> langsToActivate, final LiveRelationshipManager liveRelManager) {
        for (final String path : langsToActivate) {
            final Resource res = resolver.getResource(PWConstants.CONTENT_ROOT_PATH + PWConstants.SLASH
                    + PWConstants.LANG_MASTERS + PWConstants.SLASH + path);
            if (Objects.nonNull(res)) {
                RangeIterator rangeIterator;
                try {
                    rangeIterator = liveRelManager.getLiveRelationships(res, "", null);
                    while (rangeIterator.hasNext()) {
                        final LiveRelationship liveCopy = (LiveRelationship) rangeIterator.next();
                        LOGGER.debug("Live copies for - {}  are  {}", path, liveCopy.getTargetPath());
                        replicator.replicate(session, ReplicationActionType.ACTIVATE, liveCopy.getTargetPath());
                    }
                } catch (final WCMException | ReplicationException e) {
                    LOGGER.error("Error while finding live copies");
                }
            }
        }
    }

}
