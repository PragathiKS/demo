package com.tetrapak.supplierportal.core.servlets;

import com.day.cq.commons.Externalizer;
import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.Set;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Delete Cookie Servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.paths=" + "/bin/supplierportal/deleteCookies"
}) public class LogoutServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 5277815225105722120L;

    private static final Logger LOGGER = LoggerFactory.getLogger(LogoutServlet.class);
    static final String LOGIN_TOKEN_KEY = "login-token";
    static final String AUTH_TOKEN_KEY = "authToken";
    static final String REDIRECT_URL_KEY = "redirectURL";

    @Reference private SlingSettingsService slingSettingsService;

    @Override protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        LOGGER.debug("LogoutServlet was called");
        Cookie loginTokenCookie = request.getCookie(LOGIN_TOKEN_KEY);
        if (null != loginTokenCookie && !(isRunModeAvailable(Externalizer.AUTHOR))) {
            loginTokenCookie.setMaxAge(0);
            loginTokenCookie.setPath("/");
            response.addCookie(loginTokenCookie);
            LOGGER.debug("cookie login-token was deleted");
        }
        Cookie accTokenCookie = request.getCookie(SupplierPortalConstants.TOKEN_NAME);
        if (null != accTokenCookie) {
            accTokenCookie.setMaxAge(0);
            accTokenCookie.setPath("/");
            response.addCookie(accTokenCookie);
            LOGGER.debug("cookie acctoken was deleted");
        }
        Cookie authTokenCookie = request.getCookie(AUTH_TOKEN_KEY);
        if (null != authTokenCookie) {
            authTokenCookie.setValue(null);
            authTokenCookie.setMaxAge(0);
            authTokenCookie.setPath("/");
            authTokenCookie.setDomain(request.getServerName());
            response.addCookie(authTokenCookie);
            LOGGER.debug("cookie authToken was deleted");
        }
        Cookie samlRequestPathCookie = request.getCookie(SupplierPortalConstants.SAML_REQUEST_PATH);
        if (null != samlRequestPathCookie) {
            samlRequestPathCookie.setMaxAge(0);
            samlRequestPathCookie.setPath("/");
            response.addCookie(samlRequestPathCookie);
            LOGGER.debug("cookie samlRequestPathCookie was deleted");
        }

        Cookie aemCustomerNameCookie = request.getCookie(SupplierPortalConstants.COOKIE_NAME);
        if (null != aemCustomerNameCookie) {
            aemCustomerNameCookie.setMaxAge(0);
            aemCustomerNameCookie.setPath("/");
            aemCustomerNameCookie.setDomain("." + SupplierPortalConstants.DOMAIN_NAME);
            response.addCookie(aemCustomerNameCookie);
            LOGGER.debug("cookie " + SupplierPortalConstants.COOKIE_NAME + " was deleted");
        }

        Cookie aemCustomerEmailCookie = request.getCookie(SupplierPortalConstants.COOKIE_EMAIL);
        if (null != aemCustomerEmailCookie) {
            aemCustomerEmailCookie.setMaxAge(0);
            aemCustomerEmailCookie.setDomain("." + SupplierPortalConstants.DOMAIN_NAME);
            aemCustomerEmailCookie.setPath("/");
            response.addCookie(aemCustomerEmailCookie);
            LOGGER.debug("cookie " + SupplierPortalConstants.COOKIE_EMAIL + " was deleted");
        }

        String redirectURL = request.getParameter(REDIRECT_URL_KEY);
        try {
            if (StringUtils.isNotEmpty(redirectURL)) {
                response.sendRedirect(redirectURL);
            }
        } catch (IOException e) {
            LOGGER.error("IOException in redirecting from Logout Servlet", e);
        }
    }

    public boolean isRunModeAvailable(String key) {
        Set<String> runModesSet = getRunModes();
        if (runModesSet.contains(key)) {
            return true;
        } else {
            String runMode = System.getProperty("sling.run.modes");
            if (runMode != null && runMode.contains(key)) {
                return true;
            }
        }
        return false;
    }

    private Set<String> getRunModes() {
        return slingSettingsService.getRunModes();
    }
}
