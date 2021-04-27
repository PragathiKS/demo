package com.trs.core.services;

import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.ResourceResolver;

import com.trs.core.exceptions.TaxonomyOperationException;
import com.trs.core.reports.StatefulReport;

/**
 * 
 * This service exposes following operations :
 * 
 * 1. Fetching of Xyleme to AEM Tag mappings from DAM location
 * 
 * 2. Conversion of xyleme tag property of an asset to it's corresponding AEM
 * tag.
 * 
 */

public interface TaxonomyService {

    Map<String, String> getXylemeToAEMTagMapping(ResourceResolver resourceResolver)
            throws TaxonomyOperationException;

    List<String[]> convertXylemeTagsToAEMTags(ResourceResolver resourceResolver, String assetPath,
            Map<String, String> tagMappings);

    StatefulReport createTaxonomyServiceReport();

    void convertXylemeTagsToAEMTags(ResourceResolver resourceResolver, String assetPath,
            Map<String, String> tagMappings, StatefulReport taxonomyReport);

}
