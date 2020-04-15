package com.tetrapak.publicweb.core.models;

import com.day.cq.wcm.api.Page;
import com.tetrapak.publicweb.core.utils.PageUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import javax.annotation.PostConstruct;

/**
 * The Class SiteImproveConfigurationModel is used to fetch SiteImprove script path.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SiteImproveConfigurationModel {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SiteImproveConfigurationModel.class);

    /** The current page. */
    @ScriptVariable
    private Page currentPage;

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The site improve script. */
    private String siteImproveScriptPath;

    /** The Constant SITE_IMPROVE_PROPERTY. */
    private static final String SITE_IMPROVE_PROPERTY = "siteImproveScriptPath";

    /**
     * The init method.
     */
    @PostConstruct
    protected void init() {
        LOGGER.debug("inside init method");
        final Page languagePage = PageUtil.getLanguagePage(currentPage);
        final String pagePath = languagePage.getPath() + "/jcr:content/root/responsivegrid/siteimproveconfig";
        final Resource siteImproveConfigResource = request.getResourceResolver().getResource(pagePath);
        if (Objects.nonNull(siteImproveConfigResource)) {
            final ValueMap properties = siteImproveConfigResource.getValueMap();
            this.siteImproveScriptPath = properties.get(SITE_IMPROVE_PROPERTY, StringUtils.EMPTY);
        }
    }

    /**
     * Gets the site improve script path.
     *
     * @return the site improve script path
     */
    public String getSiteImproveScriptPath() {
        return siteImproveScriptPath;
    }
}
