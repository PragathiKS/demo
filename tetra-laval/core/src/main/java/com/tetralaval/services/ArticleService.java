package com.tetralaval.services;

import com.tetralaval.models.search.FilterModel;

import java.util.List;

/**
 * ArticleService interface
 */
public interface ArticleService {
    /**
     * type getter
     * @return type
     */
    String[] getType();

    /**
     * filterTypes getter
     * @return filterTypes
     */
    List<FilterModel> getFilterTypes();
}
