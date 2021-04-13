package com.trs.core.listeners;

import static com.day.cq.commons.Externalizer.AUTHOR;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.replication.ReplicationAction;
import com.trs.core.utils.TrsUtils;

/**
 * 
 * author : Ankit Pathak
 * 
 * This class handles the event of Deactivation & Deletion for Trs Assets.
 * 
 */

@Component(immediate = true, service = EventHandler.class, property = {
        Constants.SERVICE_DESCRIPTION + "= This class handles the event of Deactivation & Deletion for Trs Assets",
        EventConstants.EVENT_TOPIC + "=" + ReplicationAction.EVENT_TOPIC,
        EventConstants.EVENT_FILTER + "=(&(paths=/content/dam/training-services/*)(|(type=DEACTIVATE)(type=DELETE)))" })
@Designate(ocd = AssetDeactivationDeletionEventHandler.AssetDeactivationDeletionEventHandlerConfiguration.class)
public class AssetDeactivationDeletionEventHandler implements EventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(AssetDeactivationDeletionEventHandler.class);

    @Reference
    private JobManager jobManager;

    @Reference
    private SlingSettingsService slingSettingsService;

    @Reference
    private ResourceResolverFactory resolverFactory;

    private boolean enabled;

    @Activate
    @Modified
    protected void activate(AssetDeactivationDeletionEventHandlerConfiguration config) {
        enabled = config.isEnabled();
    }

    @Override
    public void handleEvent(Event event) {

        if (enabled && slingSettingsService.getRunModes().contains(AUTHOR)) {

            String[] paths = (String[]) event.getProperty("paths");

            try (ResourceResolver resourceResolver = TrsUtils.getTrsResourceResolver(resolverFactory)) {

                // Setting job properties
                Map<String, Object> jobProperties = new HashMap<>();
                jobProperties.put("replicationEventType", event.getProperty("type"));
                jobProperties.put("path", paths[0]);
                // Adding job
                jobManager.addJob("trs/asset/deactivate/delete/job", jobProperties);
                LOG.info("Trs Asset Deactivation/Deletion job added");
            } catch (LoginException e) {
                LOG.error("Error in handling deactivation/deletion event for Trs asset at path {}", paths[0], e);
            }
        }
    }

    @ObjectClassDefinition(name = "AssetDeactivationDeletionEventHandler Configuration")
    public @interface AssetDeactivationDeletionEventHandlerConfiguration {

        @AttributeDefinition(name = "Enabled", description = "Deletion/Deactivation Event Listener is enabled",type = AttributeType.BOOLEAN)
        boolean isEnabled() default true;

    }

}
