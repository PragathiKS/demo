package com.tetrapak.customerhub.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.http.Cookie;
import java.io.IOException;

/**
 * This servlet is used to delete cookies which are
 * 1. login-token
 * 2. acctoken
 * 3. authToken
 *
 * @author Nitin Kumar
 */
@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Delete Cookie Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths=" + "/bin/customerhub/delete-cookie"
        })
public class DeleteCookieServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 5277815225105722120L;

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteCookieServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        LOGGER.debug("DeleteCookieServlet was called");
        Cookie loginTokenCookie = request.getCookie("login-token");
        if (null != loginTokenCookie) {
            loginTokenCookie.setMaxAge(-1);
            response.addCookie(loginTokenCookie);
        }
        Cookie accTokenCookie = request.getCookie("acctoken");
        if (null != accTokenCookie) {
            accTokenCookie.setMaxAge(-1);
            response.addCookie(accTokenCookie);
        }
        Cookie authTokenCookie = request.getCookie("authToken");
        if (null != authTokenCookie) {
            authTokenCookie.setMaxAge(-1);
            response.addCookie(authTokenCookie);
        }
        try {
            response.sendRedirect("/customerhub/empty.html");
        } catch (IOException e) {
            LOGGER.error("unable to redirect to empty page", e);
        }
    }
}
