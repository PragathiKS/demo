package com.tetrapak.publicweb.core.utils;

import java.util.Collections;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.tetrapak.publicweb.core.constants.PWConstants;

public final class ResourceUtil {
    
    private ResourceUtil() {
        /*
         adding a private constructor to hide the implicit one
       */
    }
    /**
     * @param resolver
     * @param rootPath
     * @param resourceName
     * @param properties
     * @return resource
     * @throws PersistenceException
     */
    public static Resource createOrUpdateResource(ResourceResolver resolver, String rootPath, String resourceName,
            Map<String, Object> properties) throws PersistenceException {
        final Resource rootResource = resolver.getResource(rootPath);
        properties.values().removeAll(Collections.singleton(null));
        Resource resource = resolver.getResource(rootPath + PWConstants.SLASH + resourceName);
        if (null != rootResource && null == resource) {
            resource = resolver.create(rootResource, resourceName, properties);
            resolver.commit();
        } else if (null != resource) {
            ModifiableValueMap map = resource.adaptTo(ModifiableValueMap.class);
            map.putAll(properties);
            resolver.commit();
        }
        return resource;
    }

    /**
     * @param resolver
     * @param rootPath
     * @param resourceName
     * @param properties
     * @return resource
     * @throws PersistenceException
     * @throws RepositoryException
     */
    public static Resource createResource(ResourceResolver resolver, String rootPath, String resourceName,
            Map<String, Object> properties) throws PersistenceException {
        final Resource rootResource = resolver.getResource(rootPath);
        properties.values().removeAll(Collections.singleton(null));
        Resource resource = resolver.getResource(rootPath + PWConstants.SLASH + resourceName);
        if (null != rootResource && null == resource) {
            resource = resolver.create(rootResource, resourceName, properties);
            resolver.commit();
        }
        return resource;
    }
    
    /**
     * @param resolver
     * @param resourcePath
     * @throws PersistenceException
     */
    public static void deleteResource(ResourceResolver resolver,String resourcePath) throws PersistenceException {
        if(resolver.getResource(resourcePath) != null) {
            resolver.delete(resolver.getResource(resourcePath));
            resolver.commit();
        }
    }
}
