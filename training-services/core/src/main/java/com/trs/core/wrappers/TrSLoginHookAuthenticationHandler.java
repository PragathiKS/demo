package com.trs.core.wrappers;

import org.apache.sling.auth.core.spi.AuthenticationFeedbackHandler;
import org.apache.sling.auth.core.spi.AuthenticationHandler;
import org.apache.sling.auth.core.spi.AuthenticationInfo;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

@Component(property = { "service.ranking:Integer=8000", "path=/content/trs" })
public class TrSLoginHookAuthenticationHandler implements AuthenticationHandler, AuthenticationFeedbackHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrSLoginHookAuthenticationHandler.class);

    @Reference(target = "(service.pid=com.day.crx.security.token.impl.impl.TokenAuthenticationHandler)")
    private AuthenticationHandler wrappedAuthHandler;

    private boolean wrappedIsAuthFeedbackHandler;

    @Override
    public AuthenticationInfo extractCredentials(HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        String loginRes = httpServletRequest.getRequestURI();
        if (loginRes.endsWith(".html")) {
            LOGGER.info("TrSLoginHookAuthenticationHandler :: Resource accessed in extractCredentials" + loginRes);
            final Boolean useSecureCookie = false;
            final int expiryTime = 60 * 60 * 60 * 24;
            final String cookiePath = "/";
            Cookie cookie = new Cookie("saml_request_path", loginRes);
            cookie.setSecure(useSecureCookie);
            cookie.setMaxAge(expiryTime);
            cookie.setPath(cookiePath);
            httpServletResponse.addCookie(cookie);

        }

        // Wrap the response object to capture any calls to sendRedirect(..) so it can
        // be released in a controlled
        // manner later.
        final DeferredRedirectHttpServletResponse deferredRedirectResponse = new DeferredRedirectHttpServletResponse(
                httpServletRequest, httpServletResponse);

        return wrappedAuthHandler.extractCredentials(httpServletRequest, deferredRedirectResponse);
    }

    @Override
    public boolean requestCredentials(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException {

        return wrappedAuthHandler.requestCredentials(httpServletRequest, httpServletResponse);
    }

    @Override
    public void dropCredentials(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException {
        wrappedAuthHandler.dropCredentials(httpServletRequest, httpServletResponse);
    }

    @Override
    public void authenticationFailed(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
            AuthenticationInfo authenticationInfo) {
        // Wrap the response so we can release any previously deferred redirects
        final DeferredRedirectHttpServletResponse deferredRedirectResponse = new DeferredRedirectHttpServletResponse(
                httpServletRequest, httpServletResponse);

        if (this.wrappedIsAuthFeedbackHandler) {
            ((AuthenticationFeedbackHandler) wrappedAuthHandler).authenticationFailed(httpServletRequest,
                    deferredRedirectResponse, authenticationInfo);
        }

        try {
            deferredRedirectResponse.releaseRedirect();
        } catch (IOException e) {
            LOGGER.error("Could not release redirect", e);
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean authenticationSucceeded(HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse, AuthenticationInfo authenticationInfo) {
        boolean result = false;
        // Wrap the response so we can release any previously deferred redirects
        final DeferredRedirectHttpServletResponse deferredRedirectResponse = new DeferredRedirectHttpServletResponse(
                httpServletRequest, httpServletResponse);

        if (this.wrappedIsAuthFeedbackHandler) {
            result = ((AuthenticationFeedbackHandler) wrappedAuthHandler).authenticationSucceeded(httpServletRequest,
                    httpServletResponse, authenticationInfo);
        }

        try {
            deferredRedirectResponse.releaseRedirect();
        } catch (IOException e) {
            LOGGER.error("Could not release redirect", e);
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        return result;
    }

    @Activate
    protected void activate() {
        this.wrappedIsAuthFeedbackHandler = false;

        if (wrappedAuthHandler != null) {
            LOGGER.debug("Registered wrapped authentication feedback handler");
            this.wrappedIsAuthFeedbackHandler = wrappedAuthHandler instanceof AuthenticationFeedbackHandler;
        }
    }

    /**
     * It is not uncommon (Example: OOTB SAML Authentication Handler) for
     * response.sendRedirect(..) to be called in extractCredentials(..). When
     * sendRedirect(..) is called, the response immediately flushes causing the
     * browser to redirect.
     */
    private class DeferredRedirectHttpServletResponse extends HttpServletResponseWrapper {
        private String attributeKey = DeferredRedirectHttpServletResponse.class.getName() + "_redirectLocation";

        private HttpServletRequest request;

        public DeferredRedirectHttpServletResponse(final HttpServletRequest request,
                final HttpServletResponse response) {
            super(response);
            this.request = request;
        }

        /**
         * This method captures the redirect request and stores it to the Request so it
         * can be leveraged later.
         * 
         * @param location the location to redirect to
         */
        @Override
        public void sendRedirect(String location) {
            // Capture the sendRedirect location, and hold onto it so it can be released
            // later (via releaseRedirect())
            this.request.setAttribute(attributeKey, location);
        }

        /**
         * Invokes super.sendRedirect(..) with the value captured in
         * this.sendRedirect(..)
         * 
         * @throws IOException
         */
        public final void releaseRedirect() throws IOException {
            final String location = (String) this.request.getAttribute(attributeKey);

            if (location != null) {
                super.sendRedirect(location);
            }
        }
    }
}
