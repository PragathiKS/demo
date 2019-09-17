package com.tetrapak.customerhub.core.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

public class SendRedirectOverloadedResponse extends HttpServletResponseWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendRedirectOverloadedResponse.class);

    private HttpServletRequest httpServletRequest;
    private String prefix = null;

    public SendRedirectOverloadedResponse(HttpServletRequest inRequest
            , HttpServletResponse response) {
        super(response);
        httpServletRequest = inRequest;
        prefix = getPrefix(inRequest);
    }

    public void sendRedirect(String location) throws IOException {
        LOGGER.trace("Going originally to:" + location);
        String finalURL = null;

        if (isUrlAbsolute(location)) {
            LOGGER.trace("This url is absolute. No scheme changes will be attempted");
            finalURL = location;
        } else {
            finalURL = fixForScheme(prefix + location);
            LOGGER.trace("Going to absolute url:" + finalURL);
        }
        super.sendRedirect(finalURL);
    }

    public boolean isUrlAbsolute(String url) {
        String lowerCaseURL = url.toLowerCase();
        if (lowerCaseURL.startsWith("http") == true) {
            return true;
        } else {
            return false;
        }
    }

    public String fixForScheme(String url) {
        //alter the url here if you were to change the scheme
        return url;
    }

    public String getPrefix(HttpServletRequest request) {
        StringBuffer str = request.getRequestURL();
        String url = str.toString();
        String uri = request.getRequestURI();
        LOGGER.trace("requesturl:" + url);
        LOGGER.trace("uri:" + uri);
        int offset = url.indexOf(uri);
        String prefix = url.substring(0, offset);
        LOGGER.trace("prefix:" + prefix);
        return prefix;
    }
}
