package com.trs.core.workflow.models;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.Replicator;
import com.trs.core.services.AssetPageOpsService;
import com.trs.core.services.TaxonomyService;
import com.trs.core.services.TrsConfigurationService;
import com.trs.core.utils.TrsUtils;

/**
 * @author Ankit Pathak
 * 
 *         TrS Asset Activation Workflow Step
 * 
 */
@Component(service = WorkflowProcess.class, property = { "process.label = TrS Asset Activation" })
public class AssetPageCreationWfStep implements WorkflowProcess {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssetPageCreationWfStep.class);

    @Reference
    private Replicator replicator;

    @Reference
    private TrsConfigurationService trsConfig;

    @Reference
    private AssetPageOpsService trsAssetPageOpsService;

    @Reference
    private TaxonomyService trsTaxonomyService;

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap)
            throws WorkflowException {

        LOGGER.trace("## execute method of AssetPageCreationWfStep class starts ##");

        String payloadType = workItem.getWorkflowData().getPayloadType();

        if (StringUtils.equals(payloadType, "JCR_PATH")) {
            String path = workItem.getWorkflowData().getPayload().toString();
            LOGGER.info("Trs Asset Activation & Page Creation/Activation workflow triggered for the asset: {}", path);

            try (ResourceResolver resolver = TrsUtils.getTrsResourceResolver(resolverFactory)) {

                trsAssetPageOpsService.replicationActionOnResource(resolver, ReplicationActionType.ACTIVATE, path);
                if (TrsUtils.isVideoAsset(resolver, path)) {
                    trsAssetPageOpsService.createTrsPage(resolver, path);
                    trsAssetPageOpsService.replicationActionOnResource(resolver, ReplicationActionType.ACTIVATE,
                            trsAssetPageOpsService.getPagePathForAsset(path).get("pageResourcePath"));
                }
                trsTaxonomyService.convertXylemeTagsToAEMTags(resolver, path, null);

            } catch (Exception e) {
                LOGGER.error("Error while executing asset activation & page creation workflow for the asset {} ", path, e);
            }

        }
        LOGGER.trace("## execute method of AssetPageCreationWfStep class ends ##");
    }

}
