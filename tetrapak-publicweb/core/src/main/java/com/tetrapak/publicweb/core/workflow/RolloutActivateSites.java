package com.tetrapak.publicweb.core.workflow;

import java.io.IOException;
import java.util.Objects;

import javax.jcr.RepositoryException;
import javax.servlet.ServletException;

import org.apache.sling.api.resource.PersistenceException;
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
import com.day.cq.wcm.api.WCMException;
import com.day.cq.wcm.msm.api.LiveRelationshipManager;
import com.day.cq.wcm.msm.api.RolloutManager;
import com.tetrapak.publicweb.core.services.CreateLiveCopyService;
import com.tetrapak.publicweb.core.utils.PageUtil;

/**
 * The Class RolloutActivateSites.
 */
@Component(
        service = WorkflowProcess.class,
        property = {
                Constants.SERVICE_DESCRIPTION
                        + "=Public Web - Custom workflow process for rollout and replication of pages",
                Constants.SERVICE_VENDOR + "=Tetrapak", "process.label" + "=Rollout and Activate pages" })
public class RolloutActivateSites implements WorkflowProcess {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(RolloutActivateSites.class);

    /** The rollout manager. */
    @Reference
    private RolloutManager rolloutManager;

    /** The live rel manager. */
    @Reference
    private LiveRelationshipManager liveRelManager;
    
    /** The create live copy service. */
    @Reference
    private CreateLiveCopyService createLiveCopyService;

    /** The resource resolver factory. */
    @Reference
    private ResourceResolverFactory resourceResolverFactory;

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
        try {
	        ResourceResolver resolver = workflowSession.adaptTo(ResourceResolver.class);
	        // get the payload page from the workflow data
	        WorkflowData workflowData = workItem.getWorkflowData();
	        String payload = workflowData.getPayload().toString();
	        if (Objects.nonNull(resolver)) {
	            String language = PageUtil.getLanguagePage(resolver.getResource(payload)).getName();
	            LOGGER.info("language : {}",language);
	            createLiveCopyService.createLiveCopy(resolver, payload, rolloutManager, liveRelManager, language,false, false);
	        }
        }catch (RepositoryException | WCMException | IOException | UnsupportedOperationException | ServletException e) {
            LOGGER.error("An error occurred while creating live copy", e);
        }
    }
}