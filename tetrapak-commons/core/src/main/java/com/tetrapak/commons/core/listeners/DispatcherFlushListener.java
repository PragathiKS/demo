package com.tetrapak.commons.core.listeners;

import com.tetrapak.commons.core.services.DispatcherFlushService;
import com.tetrapak.commons.core.services.config.DispatcherFlushConfig;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.sling.api.resource.observation.ExternalResourceChangeListener;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Component(immediate = true, service = ResourceChangeListener.class)
@Designate(ocd = DispatcherFlushConfig.class)
public class DispatcherFlushListener implements ResourceChangeListener, ExternalResourceChangeListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherFlushListener.class);

    private DispatcherFlushConfig config;

    @Reference
    DispatcherFlushService dispatcherFlush;

    /**
     * activate method
     *
     * @param config config
     */
    @Activate
    @Modified
    public void activate(DispatcherFlushConfig config) {
        this.config = config;
    }

    @Override
    public void onChange(List<ResourceChange> list) {
        list.forEach(change -> {
            if (ArrayUtils.isNotEmpty(config.getPaths())) {
                for (String path : config.getPaths()) {
                    LOGGER.info("Event: {} triggered at: {}", change.getType(), path);
                    if(path.contains("i18")){
                        dispatcherFlush.flush("/libs/cq/i18n");
                    }else {
                        dispatcherFlush.flush(path);
                    }
                }
            } else if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("DispatcherPath should not be blank.");
                LOGGER.debug("Event: {} occurred at: {}; calling DispatcherFlushService", change.getType(), change.getPath());
            }
        });
    }
}
