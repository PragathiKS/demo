package com.tetrapak.publicweb.core.services;

import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.Resource;

/**
 * This is the service class for product page search functions.
 *
 */
public interface ProductPagesSearchService {

    Map<String,String> getProductPageMap(List<String> productIds, Resource resource); 
    
}
