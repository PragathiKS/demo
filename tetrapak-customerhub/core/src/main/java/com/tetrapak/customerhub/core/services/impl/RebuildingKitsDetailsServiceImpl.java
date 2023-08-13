package com.tetrapak.customerhub.core.services.impl;


import com.adobe.acs.commons.genericlists.GenericList;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.RebuildingKitsDetailsService;
import com.tetrapak.customerhub.core.services.config.RebuildingKitsDetailsConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@Component(immediate = true, service = RebuildingKitsDetailsService.class)
@Designate(ocd = RebuildingKitsDetailsConfig.class)
public class RebuildingKitsDetailsServiceImpl implements RebuildingKitsDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RebuildingKitsDetailsServiceImpl.class);
    private RebuildingKitsDetailsConfig config;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Activate
    public void activate(final RebuildingKitsDetailsConfig config) {
        this.config = config;
    }

    @Override
    public String getEbizUrl(final String selectedLanguage, final List<String> userGroups) {
        String eBizurl = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(selectedLanguage) && Objects.nonNull(userGroups)) {
            if (userGroups.contains(CustomerHubConstants.PARTS_GROUP)) {
                eBizurl = getPartServiceUrl();
            } else {
                if (StringUtils.isNotBlank(config.eBizUrlMappingPath())) {
                    final Map<String, Object> paramMap = new HashMap<>();
                    paramMap.put(ResourceResolverFactory.SUBSERVICE, CustomerHubConstants.READ_GL_USER);
                    ResourceResolver serviceResourceResolver = null;
                    try {
                        serviceResourceResolver = resourceResolverFactory.getServiceResourceResolver(paramMap);
                        PageManager pageManager = serviceResourceResolver.adaptTo(PageManager.class);
                        if (null != pageManager) {
                            Page eBizUrlMappingPath = pageManager.getPage(config.eBizUrlMappingPath());
                            if (null != eBizUrlMappingPath) {
                                GenericList ebizUrl = eBizUrlMappingPath.adaptTo(GenericList.class);
                                if (null != ebizUrl) {
                                    List<GenericList.Item> itemList = ebizUrl.getItems();
                                    Optional<String> returnedEbizUrl = itemList.stream().filter(item ->
                                            selectedLanguage.equals(item.getTitle())).map(GenericList.Item::getValue).findFirst();
                                    return returnedEbizUrl.orElse(itemList.stream().filter(item ->
                                            "default".equals(item.getTitle())).map(GenericList.Item::getValue).findFirst().orElse(null));
                                }
                            }
                        }
                    } catch (LoginException e) {
                        LOGGER.error("An error occurred while getting service resolver", e);
                        return eBizurl;
                    } finally {
                        if (serviceResourceResolver != null && serviceResourceResolver.isLive()) {
                            serviceResourceResolver.close();
                        }
                    }
                }
            }
        }
        return eBizurl;
    }

    @Override
    public String getPartServiceUrl() {
        return config.partServiceUrl();
    }
}
