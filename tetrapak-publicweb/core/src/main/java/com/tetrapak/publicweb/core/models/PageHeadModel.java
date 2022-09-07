package com.tetrapak.publicweb.core.models;

import javax.annotation.PostConstruct;

import com.tetrapak.publicweb.core.constants.PWConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.tetrapak.publicweb.core.services.BaiduMapService;

/**
 * The Class PageHeadModel.
 */
@Model(adaptables = { SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PageHeadModel {


    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The BaiduMapService. */
    @OSGiService
    private BaiduMapService baiduMapService;

    /** The Baidu Map Key. */
    private String baiduMapkey;

    /** The One Trust Servlet call api. */
    private String cookieTokenServletUrl;

    /**
     * Inits the model.
     */
    @PostConstruct
    public void initModel() {
        final String path = request.getResource().getPath();
        cookieTokenServletUrl = path.concat(".onetrustcookietoken.json");
        if(baiduMapService.getBaiduMapKey() != null && path.contains("/cn")) {
            baiduMapkey = baiduMapService.getBaiduMapKey();
        }
    }

    /**
     * Gets the Baidu Map key values.
     *
     * @return the Baidu Map key values
     */
    public String getBaiduMapkey() {
        return baiduMapkey;
    }

    /**
     * Get the One Trust Cookie Token Servlet url
     * @return cookieTokenServleturl
     */
    public String getCookieTokenServletUrl() {
        return cookieTokenServletUrl;
    }
}
