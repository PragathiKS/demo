package com.tetrapak.customerhub.core.mock;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;

import org.osgi.service.component.annotations.Component;

import java.io.IOException;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

@Component(immediate = true, service = MockQueryBuilder.class)
public class MockQueryBuilder implements QueryBuilder {

    Query query;

    public MockQueryBuilder(final Query query) {
        this.query = query;
    }

    @Override
    public void clearFacetCache() {
        // Auto-generated method stub
    }

    @Override
    public Query createQuery(final Session arg0) {
        // Auto-generated method stub
        return null;
    }

    @Override
    public Query createQuery(final PredicateGroup arg0, final Session arg1) {
        // Auto-generated method stub
        return query;
    }

    @Override
    public Query loadQuery(final String arg0, final Session arg1) throws RepositoryException, IOException {
        // Auto-generated method stub
        return null;
    }

    @Override
    public void storeQuery(final Query arg0, final String arg1, final boolean arg2, final Session arg3)
            throws RepositoryException, IOException {
        // Auto-generated method stub
    }
}
