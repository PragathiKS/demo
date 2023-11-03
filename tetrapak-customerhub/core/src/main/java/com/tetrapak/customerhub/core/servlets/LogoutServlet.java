package com.tetrapak.customerhub.core.servlets;

import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.commons.lang3.StringUtils;
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
 * This servlet is called with ajax request
 * This servlet is used to delete cookies which are
 * 1. login-token
 * 2. acctoken
 * 3. authToken
 * 4. samlRequestPathCookie
 * <p>
 * And redirects to page given in query parameter which internally redirect to sso and gets a new access token
 *
 * @author Nitin Kumar
 */
@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Delete Cookie Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths=" + "/bin/customerhub/deleteCookies"
        })
public class LogoutServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 5277815225105722120L;

    private static final Logger LOGGER = LoggerFactory.getLogger(LogoutServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        LOGGER.debug("LogoutServlet was called");
        Cookie loginTokenCookie = request.getCookie("login-token");
        if (null != loginTokenCookie && !(
                GlobalUtil.isRunModeAvailable("author"))) {
            loginTokenCookie.setMaxAge(0);
            loginTokenCookie.setPath("/");
            response.addCookie(loginTokenCookie);
            LOGGER.debug("cookie login-token was deleted");
        }
        Cookie accTokenCookie = request.getCookie("acctoken");
        if (null != accTokenCookie) {
            accTokenCookie.setMaxAge(0);
            accTokenCookie.setPath("/");
            response.addCookie(accTokenCookie);
            LOGGER.debug("cookie acctoken was deleted");
        }
        Cookie authTokenCookie = request.getCookie("authToken");
        if (null != authTokenCookie) {
            authTokenCookie.setValue(null);
            authTokenCookie.setMaxAge(0);
            authTokenCookie.setPath("/");
            authTokenCookie.setDomain(request.getServerName());
            response.addCookie(authTokenCookie);
            LOGGER.debug("cookie authToken was deleted");
        }
        Cookie samlRequestPathCookie = request.getCookie("saml_request_path");
        if (null != samlRequestPathCookie) {
            samlRequestPathCookie.setMaxAge(0);
            samlRequestPathCookie.setPath("/");
            response.addCookie(samlRequestPathCookie);
            LOGGER.debug("cookie samlRequestPathCookie was deleted");
        }

        Cookie aemCustomerNameCookie = request.getCookie("AEMCustomerName");
        if (null != aemCustomerNameCookie) {
            aemCustomerNameCookie.setMaxAge(0);
            aemCustomerNameCookie.setPath("/");
            aemCustomerNameCookie.setDomain(".tetrapak.com");
            response.addCookie(aemCustomerNameCookie);
            LOGGER.debug("cookie AEMCustomerName was deleted");
        }
        Cookie tpUserIdCookie = request.getCookie("TPCOOKIEUSERTEMP");
        if (null != tpUserIdCookie) {
            tpUserIdCookie.setMaxAge(0);
            tpUserIdCookie.setPath("/");
            tpUserIdCookie.setDomain(".tetrapak.com");
            response.addCookie(tpUserIdCookie);
            LOGGER.debug("cookie TPCOOKIEUSERTEMP was deleted");
        }
        String redirectURL = request.getParameter("redirectURL");
        try {
            if (StringUtils.isNotEmpty(redirectURL)) {
                response.sendRedirect(redirectURL);
            }
        } catch (IOException e) {
            LOGGER.error("IOException in redirecting from Logout Servlet", e);
        }
    }
}