package com.tetrapak.publicweb.core.services.impl;

import com.day.cq.commons.jcr.JcrConstants;
import com.tetrapak.publicweb.core.services.PseudoCategoryService;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * The Class PseudoCategoryServiceImpl.
 */
@Component(service = PseudoCategoryService.class, immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Designate(ocd = PseudoCategoryServiceImpl.PseudoCategoryServiceConfig.class)
public class PseudoCategoryServiceImpl implements PseudoCategoryService {

    /**
     * The Interface PseudoCategoryServiceConfig.
     */
    @ObjectClassDefinition(name = "Pseudo Categories Configuration", description = "Pseudo Categories Service Configuration")
    @interface PseudoCategoryServiceConfig {
        /**
         * Gets the pseudo categories CF root path.
         *
         * @return the pseudo categories CF root path
         */
        @AttributeDefinition(
                name = "Pseudo Categories Content Fragment Root Path",
                description = "Pseudo Categories Content Fragment Root Path")
        String getPseudoCategoriesCFRootPath() default "/content/dam/tetrapak/publicweb/contentfragment/pseudo-categories";
    }

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PseudoCategoryServiceImpl.class);

    /** The config. */
    private PseudoCategoryServiceConfig config;

    /**
     * Activate.
     *
     * @param config the config
     */
    @Activate
    public void activate(final PseudoCategoryServiceConfig config) {
        this.config = config;
    }

    @Override
    public Map<String, String> fetchPseudoCategories(final ResourceResolver resourceResolver) {
        LOGGER.debug("Inside fetchPseudoCategories method");
        final Map<String, String> pseudoCategoryMap = new HashMap<>();
        final Resource pseudoCategoryResource = resourceResolver.getResource(getPseudoCategoriesCFRootPath());
        if (Objects.nonNull(pseudoCategoryResource)) {
            final Iterator<Resource> resourceIterator = pseudoCategoryResource.listChildren();
            while (resourceIterator.hasNext()) {
                final Resource childResource = resourceIterator.next();
                if (Objects.nonNull(childResource) && !childResource.getPath().contains(JcrConstants.JCR_CONTENT)) {
                    final String dataPath = childResource.getPath() + "/jcr:content/data/master";
                    final Resource dataResource = resourceResolver.getResource(dataPath);
                    final ValueMap valueMap = dataResource.getValueMap();
                    pseudoCategoryMap.put(valueMap.get("pseudoCateoryKey", StringUtils.EMPTY),
                            valueMap.get("pseudoCateoryValue", StringUtils.EMPTY));
                }
            }
        }
        return pseudoCategoryMap;
    }

    /**
     * Gets the pseudo categories CF root path.
     *
     * @return the pseudo categories CF root path
     */
    @Override
    public String getPseudoCategoriesCFRootPath() {
        return config.getPseudoCategoriesCFRootPath();
    }
}
