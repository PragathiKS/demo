package com.tetrapak.publicweb.core.services;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.List;

/**
 * The Interface TeaserSearchService.
 */
@FunctionalInterface
public interface TeaserSearchService {

    /**
     * Gets the list of teasers.
     *
     * @param resourceResolver
     *            the resource resolver
     * @param tags
     *            the tags
     * @param rootPath
     *            the root path
     * @param limit
     *            the limit
     * @return the list of teasers
     */
    List<Page> getListOfTeasers(ResourceResolver resourceResolver, String[] tags, String rootPath, int limit);
}
