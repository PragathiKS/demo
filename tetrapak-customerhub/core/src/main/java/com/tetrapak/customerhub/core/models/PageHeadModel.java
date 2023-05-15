package com.tetrapak.customerhub.core.models;

import javax.annotation.PostConstruct;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

/**
 * The Class PageHeadModel.
 */
@Model(adaptables = { SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PageHeadModel {



    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The One Trust Servlet call api. */
    private String cookieTokenServletUrl;

    /**
     * Inits the model.
     */
    @PostConstruct
    public void initModel() {
        final String path = request.getResource().getPath();
        cookieTokenServletUrl = path.concat(CustomerHubConstants.ONETRUST_TOKEN_SERVLET_URL);
    }

    /**
     * Get the One Trust Cookie Token Servlet url
     * @return cookieTokenServleturl
     */
    public String getCookieTokenServletUrl() {
        return cookieTokenServletUrl;
    }
}
