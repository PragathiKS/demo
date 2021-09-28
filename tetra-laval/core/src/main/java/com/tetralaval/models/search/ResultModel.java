package com.tetralaval.models.search;

import java.util.List;

/**
 * Result model
 */
public class ResultModel {
    /** totalResults */
    private long totalResults;
    /** totalPages */
    private int totalPages;
    /** searchResults */
    private List<ResultItem> searchResults;

    /**
     * totalResults getter
     * @return totalResults
     */
    public long getTotalResults() {
        return totalResults;
    }

    /**
     * totalResults setter
     * @param totalResults
     */
    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

    /**
     * totalPages getter
     * @return totalPages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * totalPages setter
     * @param totalPages
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * searchResults getter
     * @return searchResults
     */
    public List<ResultItem> getSearchResults() {
        return searchResults;
    }

    /**
     * searchResults setter
     * @param searchResults
     */
    public void setSearchResults(List<ResultItem> searchResults) {
        this.searchResults = searchResults;
    }
}
