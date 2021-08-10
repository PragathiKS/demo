package com.tetralaval.services;

import com.tetralaval.models.search.FilterModel;
import com.tetralaval.models.search.ResultModel;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameterMap;

import java.util.List;
import java.util.Map;

public interface SearchResultsService {
    int getItemsPerPage();

    int getMaxResultSuggesions();

    Map<String, String> setSearchQueryMap(SlingHttpServletRequest request, RequestParameterMap params);

    List<FilterModel> getFilters(SlingHttpServletRequest request, String[] tags);

    ResultModel getResults(SlingHttpServletRequest request, Map<String, String> map);
}
