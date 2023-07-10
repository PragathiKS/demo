package com.tetrapak.publicweb.core.models.v2;

import com.day.cq.wcm.api.Page;
import com.tetrapak.publicweb.core.beans.LanguageBean;
import com.tetrapak.publicweb.core.beans.MarketBean;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.models.HeaderConfigurationModel;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import com.tetrapak.publicweb.core.utils.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * The Class HeaderModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MarketSelectorModel extends com.tetrapak.publicweb.core.models.MarketSelectorModel {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MarketSelectorModel.class);

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;
    
    /** The current page. */
    @Inject
    private Page currentPage;

    private String marketTitle;

    @Override
    public String getMarketTitle() {
        final String languagePath = LinkUtils.getRootPath(request.getPathInfo())
                + "/jcr:content/root/responsivegrid/headerconfigurationv";
        final Resource headerConfigurationResource = request.getResourceResolver().getResource(languagePath);
        if (Objects.nonNull(headerConfigurationResource)) {
            final com.tetrapak.publicweb.core.models.v2.HeaderConfigurationModel configurationModel = headerConfigurationResource
                    .adaptTo(com.tetrapak.publicweb.core.models.v2.HeaderConfigurationModel.class);
            if (Objects.nonNull(configurationModel)) {
                marketTitle = configurationModel.getMarketTitle();
            }
        }
        return marketTitle;
    }

}
