package com.tetrapak.publicweb.core.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.auth.Authenticator;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.tetrapak.publicweb.core.utils.PageUtil;

/**
 * The Class PreviewFilter.
 */
@Component(
        immediate = true,
        property = { "sling.filter.pattern=/content/tetrapak/publicweb.*", "sling.filter.scope=REQUEST",
                "service.ranking=-1" })
public class PreviewFilter implements Filter {

    /** The settings service. */
    @Reference
    private SlingSettingsService settingsService;

    /** The authenticator. */
    @Reference
    Authenticator authenticator;

    /** The is skip. */
    Boolean isSkip = true;

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
        final String previewParam = slingRequest.getParameter("preview");
        if (Boolean.FALSE.equals(isSkip) && slingRequest.getPathInfo().startsWith("/content/tetrapak/publicweb")
                && "true".equalsIgnoreCase(slingRequest.getHeader("preview")) 
                && Boolean.TRUE.equals(isSaltInValid(slingRequest, previewParam))) {
            authenticator.logout(slingRequest, slingResponse);
        }
        chain.doFilter(request, response);
    }

    /**
     * Checks if is salt in valid.
     *
     * @param slingRequest
     *            the sling request
     * @param previewParam
     *            the preview param
     * @return true, if is salt in valid
     */
    public boolean isSaltInValid(final SlingHttpServletRequest slingRequest, String previewParam) {
        boolean isSaltInValid = true;
        Resource pageContentRes = PageUtil.getCurrentPage(slingRequest.getResource()).getContentResource();
        if (pageContentRes != null && pageContentRes.getValueMap().containsKey("previewSalt")) {
            String previewSalt = (String) pageContentRes.getValueMap().get("previewSalt");
            if (previewSalt.equals(previewParam)) {
                isSaltInValid = false;
            }
        }
        return isSaltInValid;
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
        isSkip = settingsService.getRunModes().contains("publish");
    }

    /**
     * Destroy.
     */
    @Override
    public void destroy() {
        // This is override method
    }

}
