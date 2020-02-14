package com.tetrapak.publicweb.core.services;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.List;

@FunctionalInterface
public interface TeaserSearchService {
    List<Page> getListOfTeasers(ResourceResolver resourceResolver, String[] tags, String rootPath, int limit);
}
