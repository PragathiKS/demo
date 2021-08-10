package com.tetralaval.models.search;

import java.util.List;

public class ResultModel {
    private long totalResults;
    private int totalPages;
    private List<ResultItem> searchResults;

    public long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<ResultItem> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<ResultItem> searchResults) {
        this.searchResults = searchResults;
    }
}
