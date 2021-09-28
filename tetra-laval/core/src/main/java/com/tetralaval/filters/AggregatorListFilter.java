package com.tetralaval.filters;

import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.WCMMode;
import com.tetralaval.constants.TLConstants;
import com.tetralaval.services.ArticleService;
import com.tetralaval.utils.LinkUtils;
import com.tetralaval.utils.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * Aggregator List Filter
 */
@Component(
        service = Filter.class,
        name = "Custom Aggregator List Redirecting Filter",
        property = {
                "sling.filter.pattern=/content/tetrapak/tetralaval.*",
                "sling.filter.scope=REQUEST",
                "service.ranking=-2"
        }
)
public class AggregatorListFilter implements Filter {
    private static final String AGGREGATOR_PAGE_PROPERTY = "aggregatorPage";

    /**
     * Article service
     */
    @Reference
    private ArticleService articleService;

    /**
     * init method
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // This is override method
    }

    /**
     * destroy method
     */
    @Override
    public void destroy() {
        // This is override method
    }

    /**
     * Redirect to page (usually to search page) configured inside Page Properties
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof SlingHttpServletRequest && response instanceof SlingHttpServletResponse) {
            final SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
            final SlingHttpServletResponse slingResponse = (SlingHttpServletResponse) response;

            if (!(WCMMode.fromRequest(slingRequest) == WCMMode.EDIT
                    || WCMMode.fromRequest(slingRequest) == WCMMode.PREVIEW)) {
                final String redirect = getRedirect(slingRequest);

                if (StringUtils.isNotBlank(redirect)) {
                    slingResponse.sendRedirect(redirect);
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * Get redirect url page fetched from Page Properties
     * @param slingRequest
     * @return redirect url link
     */
    private String getRedirect(final SlingHttpServletRequest slingRequest) {
        String redirect = StringUtils.EMPTY;
        if (Objects.nonNull(PageUtil.getCurrentPage(slingRequest.getResource()))
                && Objects.nonNull(PageUtil.getCurrentPage(slingRequest.getResource()).getContentResource())) {
            Resource pageContentRes = PageUtil.getCurrentPage(slingRequest.getResource()).getContentResource();
            if (Objects.nonNull(pageContentRes) && pageContentRes.getValueMap().containsKey(NameConstants.PN_REDIRECT_TARGET)
                    && Boolean.parseBoolean((String) pageContentRes.getValueMap().getOrDefault(AGGREGATOR_PAGE_PROPERTY, "false"))) {
                redirect = (String) pageContentRes.getValueMap().get(NameConstants.PN_REDIRECT_TARGET);

                String[] types = articleService.getType();
                String[] params = new String[types.length];

                for (int i = 0; i < types.length; i++) {
                    params[i] = types[i].split(TLConstants.COLON)[0];
                }
                redirect = String.format("%s?contentType=%s&page=1", LinkUtils.sanitizeLink(redirect, slingRequest),
                        String.join(TLConstants.COMA, params));
            }
        }
        return redirect;
    }
}
