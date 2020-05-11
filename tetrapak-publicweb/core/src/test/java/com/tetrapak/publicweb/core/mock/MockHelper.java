package com.tetrapak.publicweb.core.mock;

import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Servlet;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class MockHelper.
 */
public class MockHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockHelper.class);

    /**
     * Utility classes, which are collections of static members, are not meant to be instantiated. It should not have
     * public constructors.
     *
     */
    private MockHelper() {
    }

    /**
     * Load query.
     *
     * @param context the context
     * @param hitPaths the hit paths
     */
    public static void loadQuery(final AemContext context, final List<String> hitPaths) {
        final List<Hit> hits = new ArrayList<>();
        for (final String hitPath : hitPaths) {
            hits.add(new MockHits(context.resourceResolver().getResource(hitPath), hitPath));
        }

        final SearchResult searchResult = new MockSearchResult(hits);
        final Query query = new MockQuery(searchResult);
        context.registerService(QueryBuilder.class, new MockQueryBuilder(query));
    }

    /**
     * Gets the servlet.
     *
     * @param <T> the generic type
     * @param context the context
     * @param servletName the servlet name
     * @return the servlet
     */
    @SuppressWarnings("unchecked")
    public static <T extends SlingSafeMethodsServlet> T getServlet(final AemContext context, final Class<T> servletName) {
        Servlet servlet;
        try {
            servlet = (Servlet) Class.forName(servletName.getName()).newInstance();
            context.registerInjectActivateService(servlet);
            for (final Servlet servletService : context.getServices(Servlet.class, null)) {
                if (servletName.getName().equals(servletService.getClass().getName())) {
                    return (T) servletService;
                }
            }
        } catch (final Exception e) {
            LOGGER.error("Exception occurred: ", e);
        }
        return null;
    }
}
