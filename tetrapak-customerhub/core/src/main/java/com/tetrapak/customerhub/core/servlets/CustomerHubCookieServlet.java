package com.tetrapak.customerhub.core.servlets;

import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.UserPreferenceService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.http.Cookie;

/**
 * Servlet to check language of the user and to create lang-code cookie
 *
 * @author Nitin Kumar
 */
@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=CustomerHub Cookie Setter Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths=" + "/bin/customerhub/setcookie"
        })
public class CustomerHubCookieServlet extends SlingSafeMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerHubCookieServlet.class);
    private static final long serialVersionUID = -528108458765402405L;

    @Reference
    private UserPreferenceService userPreferenceService;

    @Override
    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {

        Session session = request.getResourceResolver().adaptTo(Session.class);
        if (null == session) {
            LOGGER.error("CustomerHubCookieServlet exception: session is null");
            return;
        }

        final String langCode = userPreferenceService.getSavedPreferences(session.getUserID(),
                CustomerHubConstants.LANGUGAGE_PREFERENCES);
        if (StringUtils.isNotEmpty(langCode)) {
            setLanguageCookie(request, response, langCode);
        }
    }

    private void setLanguageCookie(SlingHttpServletRequest request, SlingHttpServletResponse response, String langCode) {
        Cookie cookie = new Cookie("lang-code", langCode);
        cookie.setPath("/");
        cookie.setDomain(request.getServerName());
        response.addCookie(cookie);
    }
}
