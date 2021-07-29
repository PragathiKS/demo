package com.tetralaval.filters;

import com.day.cq.wcm.api.Page;
import com.tetralaval.constants.TLConstants;
import com.tetralaval.utils.LinkUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component(
    service = Filter.class,
    name = "Custom Error Page Redirecting Filter",
    property = {
        "sling.filter.pattern=/content/tetrapak/tetralaval.*",
        "sling.filter.scope=REQUEST",
        "service.ranking=-2"
    }
)

public class ErrorPageFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(ErrorPageFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof SlingHttpServletRequest && response instanceof SlingHttpServletResponse) {
            final SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
            final SlingHttpServletResponse slingResponse = (SlingHttpServletResponse) response;
            final Resource resource = slingRequest.getResource();

            if (resource == null || (resource != null && (resource.getResourceType() == null ||
                    resource.getResourceType().equals("sling:nonexisting")))) {
                String path = Arrays.stream(slingRequest.getPathInfo().split(TLConstants.SLASH))
                        .filter(s -> !StringUtils.EMPTY.equals(s))
                        .limit(TLConstants.LANGUAGE_PAGE_LEVEL + 1)
                        .collect(Collectors.joining(TLConstants.SLASH));
                Resource languageResource = resource.getResourceResolver().resolve(TLConstants.SLASH + path);
                Page languagePage = languageResource.adaptTo(Page.class);
                slingResponse.sendRedirect(LinkUtils.sanitizeLink(languagePage.getPath() + TLConstants.SLASH + "404", slingRequest));
                return;
            }
            chain.doFilter(request, response);
        } else {
            chain.doFilter(request, response);
            return;
        }
    }

}