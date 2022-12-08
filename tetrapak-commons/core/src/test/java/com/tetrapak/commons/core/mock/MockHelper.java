package com.tetrapak.commons.core.mock;

import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;

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
     * Gets the servlet.
     *
     * @param <T>
     *            the generic type
     * @param context
     *            the context
     * @param servletName
     *            the servlet name
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

}
