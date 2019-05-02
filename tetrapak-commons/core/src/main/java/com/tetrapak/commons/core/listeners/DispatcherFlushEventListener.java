package com.tetrapak.commons.core.listeners;

import com.tetrapak.commons.core.services.DispatcherFlushService;
import com.tetrapak.commons.core.services.config.DispatcherFlushConfig;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import javax.jcr.observation.ObservationManager;

@Component(immediate = true, service = EventListener.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = DispatcherFlushConfig.class, factory = true)
public class DispatcherFlushEventListener implements EventListener {

    private DispatcherFlushConfig dispatcherFlushConfig;
    private static final Logger LOGGER = LoggerFactory.getLogger(EventListener.class);

    @Reference
    DispatcherFlushService dispatcherFlush;

    @Reference
    private SlingRepository repository;

    private Session session;
    private ObservationManager observationManager;

    @Activate
    protected void activate(ComponentContext context, DispatcherFlushConfig config) throws Exception {
        dispatcherFlushConfig = config;
        session = repository.loginService("readService", null);
        observationManager = session.getWorkspace().getObservationManager();
        observationManager.addEventListener(this, Event.PROPERTY_ADDED | Event.PROPERTY_CHANGED | Event.PROPERTY_REMOVED, dispatcherFlushConfig.absPath(), true, null,
                null, true);

        LOGGER.info("*************added JCR event listener");
    }

    protected void deactivate(ComponentContext componentContext) {
        try {
            if (observationManager != null) {
                observationManager.removeEventListener(this);
                LOGGER.info("*************removed JCR event listener");
            }
        } catch (RepositoryException re) {
            LOGGER.error("*************error removing the JCR event listener ", re);
        } finally {
            if (session != null) {
                session.logout();
                session = null;
            }
        }
    }

    @Override
    public void onEvent(EventIterator events) {
        while (events.hasNext()) {
            Event event = events.nextEvent();
            try {
                LOGGER.info("event triggered at path: {}", event.getPath());
                dispatcherFlush.flush(dispatcherFlushConfig.dispatcherPath());
            } catch (RepositoryException e) {
                LOGGER.error("RepositoryException in DispatcherFlushEventListener {}", e);
            }
        }
    }
}
