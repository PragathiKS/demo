package com.tetrapak.customerhub.core.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import javax.servlet.Servlet;

import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.i18n.ResourceBundleProvider;
import org.apache.sling.servlethelpers.MockSlingHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.replication.TransportHandler;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class MockHelper.
 */
public class MockHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockHelper.class);

    /**
     * Utility classes, which are collections of static members, are not meant to be
     * instantiated. It should not have public constructors.
     *
     */
    private MockHelper() {
    }

    /**
     * Load query.
     *
     * @param context  the context
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
     * @param             <T> the generic type
     * @param context     the context
     * @param servletName the servlet name
     * @return the servlet
     */
    @SuppressWarnings("unchecked")
    public static <T extends SlingSafeMethodsServlet> T getServlet(final AemContext context,
	    final Class<T> servletName) {
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

    /**
     * Gets the servlet.
     *
     * @param                      <T> the generic type
     * @param context              the context
     * @param transportHandlerName the servlet name
     * @return the servlet
     */
    @SuppressWarnings("unchecked")
    public static <T extends TransportHandler> T getTransportHandler(final AemContext context,
	    final Class<T> transportHandlerName) {
	final TransportHandler transportHandler;
	try {
	    transportHandler = (TransportHandler) Class.forName(transportHandlerName.getName()).newInstance();
	    context.registerInjectActivateService(transportHandler);
	    for (final TransportHandler transportHandlerService : context.getServices(TransportHandler.class, null)) {
		if (transportHandlerName.getName().equals(transportHandlerService.getClass().getName())) {
		    return (T) transportHandlerService;
		}
	    }
	} catch (final Exception e) {
	    LOGGER.error("Exception occurred: ", e);
	}
	return null;
    }

    /**
     * Method to mock ResoureBundleProvider.
     *
     * @param request the request
     * @return ResourceBundleProvider
     */
    public static final ResourceBundleProvider mockResourceBundleProvider(final MockSlingHttpServletRequest request,
	    final Map<String, String> keyValueMap) {
	return new ResourceBundleProvider() {
	    @Override
	    public ResourceBundle getResourceBundle(final String arg0, final Locale arg1) {
		if (Objects.isNull(arg0) && arg1.equals(request.getLocale())) {
		    return new ResourceBundle() {
			@Override
			protected Object handleGetObject(final String key) {
			    if (keyValueMap.containsKey(key)) {
				return keyValueMap.get(key);
			    }
			    return null;
			}

			@Override
			public Enumeration<String> getKeys() {
			    return Collections.enumeration(keySet());
			}
		    };
		}
		return null;
	    }

	    @Override
	    public ResourceBundle getResourceBundle(final Locale arg0) {
		return null;
	    }

	    @Override
	    public Locale getDefaultLocale() {
		return null;
	    }
	};
    }
}
