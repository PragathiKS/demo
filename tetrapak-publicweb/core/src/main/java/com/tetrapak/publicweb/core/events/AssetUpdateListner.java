package com.tetrapak.publicweb.core.events;

import java.util.List;
import java.util.Objects;

import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.utils.GlobalUtil;

@Component(
        immediate = true,
        configurationPolicy = ConfigurationPolicy.REQUIRE,
        service = ResourceChangeListener.class,
        property = { ResourceChangeListener.PATHS + "=" + "glob:/content/dam/tetrapak/publicweb/**/jcr:content",
                ResourceChangeListener.CHANGES + "=" + "ADDED", ResourceChangeListener.CHANGES + "=" + "CHANGED",
                ResourceChangeListener.PROPERTY_NAMES_HINT + "=jcr:lastModified"

        })
public class AssetUpdateListner implements ResourceChangeListener {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetUpdateListner.class);

    /** The resolverFactory. */
    @Reference
    private ResourceResolverFactory resolverFactory;

    @SuppressWarnings("deprecation")
    @Override
    public void onChange(List<ResourceChange> changes) {
        LOGGER.info("Coping jcr last modified to cq last modified for search");
        ResourceResolver resolver = null;
        try {
            resolver = GlobalUtil.getResourceResolverFromSubService(resolverFactory);
            if (Objects.nonNull(resolver)) {
                for (ResourceChange change : changes) {
                    if (change.getChangedPropertyNames().contains(PWConstants.JCR_LAST_MODIFIED)
                            || change.getAddedPropertyNames().contains(PWConstants.JCR_LAST_MODIFIED)) {
                        LOGGER.info("Asset Path :: {}", change.getPath());
                        Resource assetJcrResource = resolver.getResource(change.getPath());
                        if (Objects.nonNull(assetJcrResource)
                                && assetJcrResource.getValueMap().containsKey(PWConstants.JCR_LAST_MODIFIED)) {
                            ModifiableValueMap map = assetJcrResource.adaptTo(ModifiableValueMap.class);
                            map.put(PWConstants.CQ_LAST_MODIFIED,
                                    assetJcrResource.getValueMap().get(PWConstants.JCR_LAST_MODIFIED));
                            resolver.commit();
                        }
                    }
                }
            }
        } catch (PersistenceException e) {
            LOGGER.error("Exception in AssetUpdateListner ::", e.getMessage(), e);

        } finally {
            if (resolver != null && resolver.isLive()) {
                resolver.close();
            }
        }
    }

}
