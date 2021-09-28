package com.tetralaval.services;

import com.tetralaval.models.search.FilterModel;
import com.tetralaval.models.search.ResultModel;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameterMap;

import java.util.List;
import java.util.Map;

/**
 * SearchResultsService interface
 */
public interface SearchResultsService {
    /**
     * itemsPerPage getter
     * @return itemsPerPage
     */
    int getItemsPerPage();

    /**
     * maxResultSuggestions getter
     * @return maxResultSuggestions
     */
    int getMaxResultSuggestions();

    /**
     * Return search query map
     * @param request
     * @param params
     * @return
     */
    Map<String, String> setSearchQueryMap(SlingHttpServletRequest request, RequestParameterMap params);

    /**
     * Get filters
     * @param request
     * @param tags
     * @return list of FilterModel
     */
    List<FilterModel> getFilters(SlingHttpServletRequest request, String[] tags);

    /**
     * Get results
     * @param request
     * @param map
     * @return ResultModel
     */
    ResultModel getResults(SlingHttpServletRequest request, Map<String, String> map);

    /**
     * Set mediaId based on mediaLabel
     * @param mediaLabel
     * @return mediaId
     */
    String setMediaId(String mediaLabel);
}
