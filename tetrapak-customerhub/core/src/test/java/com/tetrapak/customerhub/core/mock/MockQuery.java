package com.tetrapak.customerhub.core.mock;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.eval.PredicateEvaluator;
import com.day.cq.search.facets.Bucket;
import com.day.cq.search.result.SearchResult;

/**
 * @author sku154
 */
public class MockQuery implements Query {

    SearchResult searchResult;

    public MockQuery(final SearchResult searchResult) {
        this.searchResult = searchResult;
    }

    @Override
    public boolean getExcerpt() {
        // Auto-generated method stub
        return false;
    }

    @Override
    public long getHitsPerPage() {
        // Auto-generated method stub
        return 0;
    }

    @Override
    public PredicateGroup getPredicates() {
        // Auto-generated method stub
        return null;
    }

    @Override
    public SearchResult getResult() {
        // Auto-generated method stub
        return searchResult;
    }

    @Override
    public long getStart() {
        // Auto-generated method stub
        return 0;
    }

    @Override
    public Query refine(final Bucket arg0) {
        // Auto-generated method stub
        return null;
    }

    @Override
    public void registerPredicateEvaluator(final String arg0, final PredicateEvaluator arg1) {
        // Auto-generated method stub
    }

    @Override
    public void setExcerpt(final boolean arg0) {
        // Auto-generated method stub
    }

    @Override
    public void setHitsPerPage(final long arg0) {
        // Auto-generated method stub
    }

    @Override
    public void setStart(final long arg0) {
        // Auto-generated method stub
    }
}
