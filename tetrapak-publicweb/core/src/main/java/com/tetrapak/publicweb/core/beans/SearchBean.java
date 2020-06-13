package com.tetrapak.publicweb.core.beans;

import java.util.List;

/**
 * The Class SearchBean.
 */
public class SearchBean {

    /** The total results. */
    private long totalResults;

    /** The total pages. */
    private int totalPages;

    /** The search results. */
    private List<SearchResultBean> searchResults;

    /**
     * Gets the total results.
     *
     * @return the total results
     */
    public long getTotalResults() {
        return totalResults;
    }

    /**
     * Sets the total results.
     *
     * @param totalResults
     *            the new total results
     */
    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

    /**
     * Gets the total pages.
     *
     * @return the total pages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * Sets the total pages.
     *
     * @param totalPages
     *            the new total pages
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * Gets the search results.
     *
     * @return the search results
     */
    public List<SearchResultBean> getSearchResults() {
        return searchResults;
    }

    /**
     * Sets the search results.
     *
     * @param searchResults
     *            the new search results
     */
    public void setSearchResults(List<SearchResultBean> searchResults) {
        this.searchResults = searchResults;
    }

}
