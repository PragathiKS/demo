package com.tetralaval.services;

import com.tetralaval.models.search.FilterModel;

import java.util.List;

public interface ArticleService {
    String[] getType();

    List<FilterModel> getFilterTypes();
}
