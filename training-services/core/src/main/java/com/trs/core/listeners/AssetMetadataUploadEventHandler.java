package com.trs.core.listeners;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * author : Ankit Pathak
 * 
 * This class handles the event of OOTB Asset Metadata upload filtered for Trs
 * Assets.
 * 
 */

@Component(immediate = true, service = EventHandler.class, property = {
        Constants.SERVICE_DESCRIPTION
                + "= Event handler for the event of OOTB Asset Metadata upload filtered for TrS Assets",
        EventConstants.EVENT_TOPIC + "=org/apache/sling/event/notification/job/FINISHED",
        EventConstants.EVENT_FILTER + "=(event.job.topic=async/importMetadata)" })
public class AssetMetadataUploadEventHandler implements EventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssetMetadataUploadEventHandler.class);
    
    @Reference
    private JobManager jobManager;


    @Override
    public void handleEvent(Event event) {

        if (FilenameUtils.getBaseName(event.getProperty("inputFile").toString()).startsWith("trs-metadata")) {
            LOGGER.info("Trs Asset metadata uploaded");

            Map<String, Object> jobProperties = new HashMap<>();
            jobProperties.put("inputFilePath", event.getProperty("inputFile"));

            jobManager.addJob("trs/page/creation/job", jobProperties);

            LOGGER.info("Trs Asset Metadata upload job added");

        }
    }

}
