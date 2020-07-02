package com.tetrapak.publicweb.core.events;

import com.day.cq.commons.jcr.JcrConstants;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.utils.GlobalUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

@Component(
        immediate = true,
        configurationPolicy = ConfigurationPolicy.REQUIRE,
        service = ResourceChangeListener.class,
        property = {
                ResourceChangeListener.PATHS + "=" + "glob:/content/dam/tetrapak/publicweb/**/jcr:content",
                ResourceChangeListener.PATHS + "=" + "glob:/content/dam/tetrapak/products/**/jcr:content",
                ResourceChangeListener.PATHS + "=" + "glob:/content/dam/tetrapak/media-box/**/jcr:content",
                ResourceChangeListener.CHANGES + "=" + "ADDED", ResourceChangeListener.CHANGES + "=" + "CHANGED",
                ResourceChangeListener.PROPERTY_NAMES_HINT + "=jcr:lastModified"

        })
public class AssetUpdateListner implements ResourceChangeListener {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetUpdateListner.class);

    /** The resolverFactory. */
    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    public void onChange(final List<ResourceChange> changes) {
        LOGGER.info("Copying jcr last modified to cq last modified for search");
        try (final ResourceResolver resolver = GlobalUtil.getResourceResolverFromSubService(resolverFactory)) {
            if (Objects.nonNull(resolver)) {
                for (final ResourceChange change : changes) {
                    if (change.getPath().contains("jcr:content")) {
                        final String assetJcrContentPath = StringUtils.substringBefore(change.getPath(),
                                JcrConstants.JCR_CONTENT) + JcrConstants.JCR_CONTENT;
                        LOGGER.info("Asset Path :: {}", assetJcrContentPath);
                        final Resource assetJcrResource = resolver.getResource(assetJcrContentPath);
                        final ValueMap valueMap = assetJcrResource.getValueMap();
                        LOGGER.info("Asset Res :: {}",assetJcrResource);
                        if (Objects.nonNull(assetJcrResource) && valueMap.containsKey(PWConstants.JCR_LAST_MODIFIED)) {
                            final ModifiableValueMap map = assetJcrResource.adaptTo(ModifiableValueMap.class);
                            LOGGER.info("Asset map :: {}", map);
                            LOGGER.info("Asset value map :: {}", valueMap);
                            LOGGER.info("Asset value map prop :: {}", valueMap.get(PWConstants.JCR_LAST_MODIFIED));
                            if(map.containsKey(PWConstants.CQ_LAST_MODIFIED)) {
                                map.remove(PWConstants.CQ_LAST_MODIFIED);
                            }
                            map.put(PWConstants.CQ_LAST_MODIFIED, valueMap.get(PWConstants.JCR_LAST_MODIFIED));
                            resolver.commit();
                            break;
                        }
                    }
                }
            }
        } catch (final PersistenceException e) {
            LOGGER.error("Exception in AssetUpdateListner ::", e.getMessage(), e);
        }
    }
}
