package com.trs.core.jobs;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.wcm.api.PageManager;
import com.trs.core.services.AssetPageOpsService;
import com.trs.core.utils.TrsUtils;

/**
 * author : Ankit Pathak
 * 
 * This job consumer performs the following tasks :
 * 
 * 1. Deactivation of an asset's page when asset is deactivated
 * 
 * 2. Deletion of an asset's page when asset is deleted
 * 
 */
@Component(service = JobConsumer.class, immediate = true, property = {
        Constants.SERVICE_DESCRIPTION + "=Trs Asset Deactivation/Deletion event handler job",
        JobConsumer.PROPERTY_TOPICS + "=trs/asset/deactivate/delete/job" })
public class AssetDeactivationDeletionEventJob implements JobConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssetDeactivationDeletionEventJob.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private AssetPageOpsService trsAssetPageOpsService;

    @Override
    public JobResult process(Job job) {

        LOGGER.trace("Trs Asset Deactivation/Deletion event handler job starts");
        try (ResourceResolver resourceResolver = TrsUtils.getTrsResourceResolver(resolverFactory)) {

            String replicationEventType = (String) job.getProperty("replicationEventType");
            LOGGER.info("Action requested : " + replicationEventType);
            String pagePath = trsAssetPageOpsService.getPagePathForAsset(job.getProperty("path").toString())
                    .get("pageResourcePath");
            Resource pageResource = resourceResolver.getResource(pagePath);

            if (pageResource != null && replicationEventType.equals(ReplicationActionType.DEACTIVATE.toString())) {
                trsAssetPageOpsService.replicationActionOnResource(resourceResolver, ReplicationActionType.DEACTIVATE,
                        pagePath);
            } else if (pageResource != null && replicationEventType.equals(ReplicationActionType.DELETE.toString())) {
                trsAssetPageOpsService.replicationActionOnResource(resourceResolver, ReplicationActionType.DELETE,
                        pagePath);
                PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
                pageManager.delete(pageResource, false);
            }

            LOGGER.trace("Trs Asset Deactivation/Deletion event handler job ends");
            return JobConsumer.JobResult.OK;

        } catch (Exception e) {
            LOGGER.error("Error in Trs Asset Deactivation/Deletion event handler job", e);
            return JobResult.CANCEL;
        }
    }

}
