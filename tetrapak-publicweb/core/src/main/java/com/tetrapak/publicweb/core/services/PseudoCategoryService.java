package com.tetrapak.publicweb.core.services;

import org.apache.sling.api.resource.ResourceResolver;

import java.util.Map;

public interface PseudoCategoryService {

    Map<String, String> fetchPseudoCategories(final ResourceResolver resourceResolver);

    String getPseudoCategoriesCFRootPath();
}
