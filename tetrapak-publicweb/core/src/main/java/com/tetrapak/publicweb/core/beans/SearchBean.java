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
     * Sets the total results.
     *
     * @param totalResults
     *            the new total results
     */
    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
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
     * Sets the search results.
     *
     * @param searchResults
     *            the new search results
     */
    public void setSearchResults(List<SearchResultBean> searchResults) {
        this.searchResults = searchResults;
    }

}
