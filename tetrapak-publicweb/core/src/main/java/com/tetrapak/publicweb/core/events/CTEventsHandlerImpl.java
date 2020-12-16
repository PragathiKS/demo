package com.tetrapak.publicweb.core.events;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
        immediate = true,
        service = EventHandler.class,
        property = { EventConstants.EVENT_TOPIC + "=com/claytablet/TRANSLATION" })
public class CTEventsHandlerImpl implements EventHandler {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CTEventsHandlerImpl.class);

    @Override
    public void handleEvent(Event event) {
        EventDetailType eventType = (EventDetailType) event.getProperty("EventDetailType");
        String details = event.getProperty("TranslationItemDetails").toString();
        LOGGER.info("CTEventsHandlerImpl eventType {}", eventType);
        LOGGER.info("CTEventsHandlerImpl details {}", details);
    }

}
