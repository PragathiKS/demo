package com.tetrapak.customerhub.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;

/**
 * Auth Checker Servlet performs the authentication and authorization of the user
 *
 * @author Nitin Kumar
 */
@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Auth Checker Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_HEAD,
                "sling.servlet.paths=" + "/bin/customerhub/permissioncheck"
        })
public class AuthCheckerServlet extends SlingSafeMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthCheckerServlet.class);
    private static final long serialVersionUID = -8381109083575997038L;

    @Override
    public void doHead(SlingHttpServletRequest request, SlingHttpServletResponse response) {

        Session session = request.getResourceResolver().adaptTo(Session.class);
        if (null == session) {
            LOGGER.error("auth checker servlet exception: session is null");
            return;
        }

        String uri = request.getParameter("uri");
        performPermissionCheck(response, uri, session);
    }

    private void performPermissionCheck(SlingHttpServletResponse response, String uri, Session session) {
        try {
            session.checkPermission(uri, Session.ACTION_READ);
            LOGGER.info("auth checker says OK");
            response.setStatus(SlingHttpServletResponse.SC_OK);
        } catch (RepositoryException e) {
            LOGGER.info("auth checker says READ access DENIED! ", e);
            response.setStatus(SlingHttpServletResponse.SC_FORBIDDEN);
        }
    }
}
