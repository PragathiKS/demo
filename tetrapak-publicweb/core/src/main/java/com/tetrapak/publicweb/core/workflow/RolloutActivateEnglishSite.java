package com.tetrapak.publicweb.core.workflow;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.wcm.msm.api.LiveRelationshipManager;
import com.day.cq.wcm.msm.api.RolloutManager;
import com.tetrapak.publicweb.core.services.CreateLiveCopyService;

/**
 * The Class RolloutActivateEnglishSite.
 */
@Component(
        service = WorkflowProcess.class,
        property = {
                Constants.SERVICE_DESCRIPTION
                        + "=Public Web - Custom workflow process for rollout and replication of english site pages",
                Constants.SERVICE_VENDOR + "=Tetrapak", "process.label" + "=Rollout and Activate English Site" })
public class RolloutActivateEnglishSite implements WorkflowProcess {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(RolloutActivateEnglishSite.class);

    /** The rollout manager. */
    @Reference
    private RolloutManager rolloutManager;

    /** The live rel manager. */
    @Reference
    private LiveRelationshipManager liveRelManager;
    @Reference
    private CreateLiveCopyService createLiveCopyService;

    /** The resource resolver factory. */
    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    /** The resolver. */
    private ResourceResolver resolver;

    /**
     * Execute.
     *
     * @param workItem
     *            the work item
     * @param workflowSession
     *            the workflow session
     * @param paramMetaDataMap
     *            the param meta data map
     */
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap paramMetaDataMap) {
        LOGGER.debug("inside execute method");
        // get the payload page from the workflow data
        WorkflowData workflowData = workItem.getWorkflowData();
        String payload = workflowData.getPayload().toString();
        resolver = workflowSession.adaptTo(ResourceResolver.class);
        // Get Instance of PageManager
        createLiveCopyService.createLiveCopy(resolver, payload, rolloutManager, liveRelManager);

    }
}
