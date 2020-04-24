package com.tetrapak.publicweb.core.mock;

import javax.servlet.Servlet;

import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class MockHelper.
 */
public class MockHelper {

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
    public static <T extends SlingSafeMethodsServlet> T getServlet(AemContext context, Class<T> servletName) {
        Servlet servlet;
        try {
            servlet = (Servlet) Class.forName(servletName.getName()).newInstance();
            context.registerInjectActivateService(servlet);
            for (Servlet servletService : context.getServices(Servlet.class, null)) {
                if (servletName.getName().equals(servletService.getClass().getName())) {
                    return (T) servletService;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
