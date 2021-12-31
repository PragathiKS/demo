package com.tetralaval.services;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameterMap;

import java.util.Map;

public interface ContentChangeService {
    /**
     * Get results
     * @param request
     * @param params
     * @return Map
     */
    Map<String, Object> getResults(SlingHttpServletRequest request, RequestParameterMap params);
}
