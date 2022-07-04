package com.tetrapak.customerhub.core.mock;

import com.day.cq.search.facets.Facet;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.ResultPage;
import com.day.cq.search.result.SearchResult;

import org.apache.sling.api.resource.Resource;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class MockSearchResult implements SearchResult {

    List<Hit> hits;

    public MockSearchResult(final List<Hit> hits) {
        this.hits = hits;
    }

    @Override
    public String getExecutionTime() {
        // Auto-generated method stub
        return null;
    }

    @Override
    public long getExecutionTimeMillis() {
        // Auto-generated method stub
        return 0;
    }

    @Override
    public Map<String, Facet> getFacets() throws RepositoryException {
        // Auto-generated method stub
        return null;
    }

    @Override
    public String getFilteringPredicates() {
        // Auto-generated method stub
        return null;
    }

    @Override
    public List<Hit> getHits() {
        // Auto-generated method stub
        return hits;
    }

    @Override
    public long getHitsPerPage() {
        // Auto-generated method stub
        return 0;
    }

    @Override
    public ResultPage getNextPage() {
        // Auto-generated method stub
        return null;
    }

    @Override
    public Iterator<Node> getNodes() {
        // Auto-generated method stub
        return null;
    }

    @Override
    public ResultPage getPreviousPage() {
        // Auto-generated method stub
        return null;
    }

    @Override
    public String getQueryStatement() {
        // Auto-generated method stub
        return null;
    }

    @Override
    public Iterator<Resource> getResources() {
        // Auto-generated method stub
        return null;
    }

    @Override
    public List<ResultPage> getResultPages() {
        // Auto-generated method stub
        return null;
    }

    @Override
    public long getStartIndex() {
        // Auto-generated method stub
        return 0;
    }

    @Override
    public long getTotalMatches() {
        // Auto-generated method stub
        return 0;
    }

    @Override
    public boolean hasMore() {
        // Auto-generated method stub
        return false;
    }
}
