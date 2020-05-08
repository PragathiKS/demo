package com.tetrapak.publicweb.core.workflow;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.tetrapak.publicweb.core.constants.PWConstants;

@Component(
        service = WorkflowProcess.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Public Web - Custom workflow process for replication for pxp upload",
                Constants.SERVICE_VENDOR + "=Tetrapak", "process.label" + "=PW Asset Replication for PXP upload" })
public class AssetReplicationWorkflowProcess implements WorkflowProcess {

    @Reference
    private Replicator replicator;

    private static final String TYPE_PROCESS_ARGS = "PROCESS_ARGS";
    private static final String DEFAULT_PATH_TO_CHECK = "/content/dam/tetrapak/publicweb/pxp";
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetReplicationWorkflowProcess.class);

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap paramMetaDataMap) {
        LOGGER.debug("inside execute");
        final String payloadPath = workItem.getWorkflowData().getPayload().toString();
        final String metadataNodePath = payloadPath.replace("renditions/original", "metadata");
        final ResourceResolver resourceResolver = workflowSession.adaptTo(ResourceResolver.class);
        String pathToCheck = DEFAULT_PATH_TO_CHECK;
        if (paramMetaDataMap.containsKey(TYPE_PROCESS_ARGS)) {
            pathToCheck = paramMetaDataMap.get(TYPE_PROCESS_ARGS, String.class);
        }
        try {
            if (resourceResolver != null) {
                replicateAsset(metadataNodePath, resourceResolver, pathToCheck);
            }
        } catch (final ReplicationException e) {
            LOGGER.error("Error occured while asset replication :: {}", e.getMessage(), e);
        }
    }

    /**
     * Replicate the asset in payload
     *
     * @param metadataNodePath
     * @param resourceResolver
     * @param pathToCheck
     *
     * @throws RepositoryException
     * @throws ReplicationException
     */
    private void replicateAsset(final String metadataNodePath, final ResourceResolver resourceResolver,
            final String pathToCheck) throws ReplicationException {
        final Resource resource = resourceResolver.getResource(metadataNodePath);
        final ModifiableValueMap map = resource.adaptTo(ModifiableValueMap.class);
        if (metadataNodePath.contains(pathToCheck) && map.containsKey(PWConstants.REPLICATION_PXP)) {
            map.remove(PWConstants.REPLICATION_PXP);
            final String assetPath = metadataNodePath.replace("/jcr:content/metadata", "");
            LOGGER.debug("trigerring replication : assetPath {} ", assetPath);
            replicator.replicate(resourceResolver.adaptTo(Session.class), ReplicationActionType.ACTIVATE, assetPath);
            LOGGER.debug("asset has been replicated : assetPath {} ", assetPath);
        }
    }

}
