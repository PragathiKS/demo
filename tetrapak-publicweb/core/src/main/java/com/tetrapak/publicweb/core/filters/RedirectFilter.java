package com.tetrapak.publicweb.core.filters;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.day.cq.wcm.api.WCMMode;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import com.tetrapak.publicweb.core.utils.PageUtil;

/**
 * The Class RedirectFilter.
 */
@Component(
        immediate = true,
        property = { "sling.filter.pattern=/content/tetrapak/publicweb.*", "sling.filter.scope=REQUEST",
                "service.ranking=-2" })
public class RedirectFilter implements Filter {

    /** The settings service. */
    @Reference
    private SlingSettingsService settingsService;

    /**
     * Do filter.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param chain
     *            the chain
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws ServletException
     *             the servlet exception
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        final SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
        final SlingHttpServletResponse slingResponse = (SlingHttpServletResponse) response;
        final String redirect = getRedirect(slingRequest);
        if (Boolean.FALSE.equals(WCMMode.fromRequest(slingRequest) == WCMMode.EDIT
                || WCMMode.fromRequest(slingRequest) == WCMMode.PREVIEW) && StringUtils.isNotBlank(redirect)) {
            slingResponse.sendRedirect(LinkUtils.sanitizeLink(redirect, slingRequest));
            return;
        }
        chain.doFilter(request, response);
    }


    /**
     * Gets the redirect.
     *
     * @param slingRequest the sling request
     * @return the redirect
     */
    public String getRedirect(final SlingHttpServletRequest slingRequest) {
        String redirect = StringUtils.EMPTY;
        if (Objects.nonNull(PageUtil.getCurrentPage(slingRequest.getResource()))
                && Objects.nonNull(PageUtil.getCurrentPage(slingRequest.getResource()).getContentResource())) {
            Resource pageContentRes = PageUtil.getCurrentPage(slingRequest.getResource()).getContentResource();
            if (Objects.nonNull(pageContentRes) && pageContentRes.getValueMap().containsKey("cq:redirectTarget")) {
                redirect = (String) pageContentRes.getValueMap().get("cq:redirectTarget");
                if (redirect.endsWith("/search") && (pageContentRes.getPath().contains("/news-and-events/news-room")
                        || pageContentRes.getPath().contains("/news-and-events/newsarchive"))) {
                    redirect = redirect + "?contentType=news&page=1";
                }
            }
        }
        return redirect;
    }

    /**
     * Inits the.
     *
     * @param filterConfig
     *            the filter config
     * @throws ServletException
     *             the servlet exception
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
     // This is override method
    }

    /**
     * Destroy.
     */
    @Override
    public void destroy() {
        // This is override method
    }

}
