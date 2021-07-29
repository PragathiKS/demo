package com.tetrapak.publicweb.core.models;

import com.tetrapak.publicweb.core.services.BaiduMapService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * The Class PageHeadModel.
 */
@Model(adaptables = { SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PageHeadModel {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PageHeadModel.class);

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The BaiduMapService. */
    @OSGiService
    private BaiduMapService baiduMapService;

    /** The Baidu Map Key. */
    private String baiduMapkey;

    /**
     * Inits the model.
     */
    @PostConstruct
    public void initModel() {
        final String path = request.getResource().getPath();
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
}
