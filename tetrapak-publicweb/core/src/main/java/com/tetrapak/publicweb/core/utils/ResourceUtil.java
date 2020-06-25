package com.tetrapak.publicweb.core.utils;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.tetrapak.publicweb.core.constants.PWConstants;

/**
 * The Class ResourceUtil.
 */
public final class ResourceUtil {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceUtil.class);

    /**
     * Instantiates a new resource util.
     */
    private ResourceUtil() {
        /*
         * adding a private constructor to hide the implicit one
         */
    }

    /**
     * Creates the or update resource.
     *
     * @param resolver
     *            the resolver
     * @param rootPath
     *            the root path
     * @param resourceName
     *            the resource name
     * @param properties
     *            the properties
     * @return resource
     * @throws PersistenceException
     *             the persistence exception
     */
    public static Resource createOrUpdateResource(ResourceResolver resolver, String rootPath, String resourceName,
            Map<String, Object> properties) throws PersistenceException {
        final Resource rootResource = resolver.getResource(rootPath);
        properties.values().removeAll(Collections.singleton(null));
        Resource resource = resolver.getResource(rootPath + PWConstants.SLASH + resourceName);
        if (null != rootResource && null == resource) {
            resource = resolver.create(rootResource, resourceName, properties);
        } else if (null != resource) {
            ModifiableValueMap map = resource.adaptTo(ModifiableValueMap.class);
            map.putAll(properties);
        }
        return resource;
    }

    /**
     * Creates the resource.
     *
     * @param resolver
     *            the resolver
     * @param rootPath
     *            the root path
     * @param resourceName
     *            the resource name
     * @param properties
     *            the properties
     * @return resource
     * @throws PersistenceException
     *             the persistence exception
     */
    public static Resource createResource(ResourceResolver resolver, String rootPath, String resourceName,
            Map<String, Object> properties) throws PersistenceException {
        final Resource rootResource = resolver.getResource(rootPath);
        properties.values().removeAll(Collections.singleton(null));
        Resource resource = resolver.getResource(rootPath + PWConstants.SLASH + resourceName);
        if (null != rootResource && null == resource) {
            resource = resolver.create(rootResource, resourceName, properties);
        }
        return resource;
    }

    /**
     * Replicate all child resource.
     *
     * @param replicator
     *            the replicator
     * @param session
     *            the session
     * @param rootRes
     *            the root res
     * @throws ReplicationException
     *             the replication exception
     */
    public static void replicateChildResources(Replicator replicator, Session session, Resource rootRes)
            throws ReplicationException {
        if (rootRes == null || !rootRes.hasChildren()) {
            return;
        }
        Iterator<Resource> itr = rootRes.listChildren();
        while (itr.hasNext()) {
            Resource res = itr.next();
            replicator.replicate(session, ReplicationActionType.ACTIVATE, res.getPath());
            replicateChildResources(replicator, session, res);
        }
    }

    /**
     * replicate products.
     *
     * @param replicator
     *            the replicator
     * @param session
     *            the session
     * @param path
     *            the path
     */
    public static void deactivatePath(Replicator replicator, Session session, String path) {
        try {
            replicator.replicate(session, ReplicationActionType.DEACTIVATE, path);
        } catch (ReplicationException e) {
            LOGGER.error("Replication Exception in activating PXP products", e.getMessage(), e);
        }
    }

    /**
     * Delete resource.
     *
     * @param resolver
     *            the resolver
     * @param resourcePath
     *            the resource path
     */
    public static void deleteResource(ResourceResolver resolver, String resourcePath) {
        if (resolver.getResource(resourcePath) != null) {
            try {
                resolver.delete(resolver.getResource(resourcePath));
            } catch (PersistenceException e) {
                LOGGER.error("PersistenceException Exception in deleting PXP products", e.getMessage(), e);
            }
        }
    }

    /**
     * Delete resource.
     *
     * @param resolver
     *            the resolver
     * @param session
     *            the session
     * @param resourcePath
     *            the resource path
     */
    public static void deleteResource(ResourceResolver resolver, Session session, String resourcePath) {
        if (resolver.getResource(resourcePath) != null && Objects.nonNull(session)) {
            try {
                resolver.delete(resolver.getResource(resourcePath));
                session.save();
            } catch (PersistenceException e) {
                LOGGER.error("PersistenceException Exception in deleting PXP products", e.getMessage(), e);

            } catch (RepositoryException e) {
                LOGGER.error("RepositoryException Exception in deleting PXP products", e.getMessage(), e);
            }
        }
    }
}
