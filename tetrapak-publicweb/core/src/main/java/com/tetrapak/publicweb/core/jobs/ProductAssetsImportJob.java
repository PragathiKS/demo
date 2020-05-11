
package com.tetrapak.publicweb.core.jobs;

import java.io.IOException;
import java.io.InputStream;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.api.AssetManager;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.tetrapak.publicweb.core.beans.pxp.AssetDetail;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.AssetImportService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;

/**
 * Sling job to add assets to AEM DAM
 * 
 * @author Akash Bansal
 *
 */
@Component(
        immediate = true,
        service = JobConsumer.class,
        property = { JobConsumer.PROPERTY_TOPICS + "=pxp/dam/assets/create" })
public class ProductAssetsImportJob implements JobConsumer {

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private Replicator replicator;

    @Reference
    private AssetImportService assetimportservice;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductAssetsImportJob.class);

    public JobResult process(final Job job) {

        Session session = null;
        // using try with resources to close the resources after
        try (ResourceResolver resourceResolver = GlobalUtil.getResourceResolverFromSubService(resolverFactory)) {
            if (resourceResolver != null) {
                session = resourceResolver.adaptTo(Session.class);
                if (null != session) {
                    String sourceurl = job.getProperty("sourceurl").toString();
                    String finalDAMPath = job.getProperty("finalDAMPath").toString();
                    LOGGER.debug("final DAMPath {}", finalDAMPath);
                    return processAsset(session, resourceResolver, sourceurl, finalDAMPath);
                }
            }
        }
        return JobResult.FAILED;
    }

    /**
     * Process the asset create request received in Sling job post session validation
     * 
     * @param session
     * @param resourceResolver
     * @param sourceurl
     * @param finalDAMPath
     */
    private JobResult processAsset(Session session, ResourceResolver resourceResolver, String sourceurl,
            String finalDAMPath) {
        // check if resource already exists
        Resource resource = resourceResolver.getResource(finalDAMPath);
        if (resource == null) {
            LOGGER.debug("Asset doesn't exist");
            // fetch the Asset binary from given URL
            AssetDetail assetDetail = assetimportservice.getAssetDetailfromInputStream(sourceurl);
            if (null != assetDetail) {
                try (InputStream is = assetDetail.getIs()) {
                    // upload the assets in AEM DAM
                    return createAsset(finalDAMPath, assetDetail.getContentType(), is, session, resourceResolver);
                } catch (IOException e) {
                    LOGGER.error("Error while closing Input Stream for damPath {} \nSource URL {}", finalDAMPath,
                            sourceurl, e);
                }
            }
        } else {
            LOGGER.info("Asset already exists, skipping Asset creation. asset Path :  {} ", finalDAMPath);
            return JobResult.CANCEL;
        }
        return JobResult.FAILED;
    }

    /**
     * Create asset in DAM returns the JobResult based on asset creation result
     * 
     * @param finalDAMPath
     * @param assetDetail
     * @param session
     * @param resourceResolver
     * @return
     */
    private JobResult createAsset(String finalDAMPath, String contentType, InputStream is, Session session,
            ResourceResolver resourceResolver) {
        LOGGER.debug("Inside creatin assets {}", finalDAMPath);
        AssetManager assetManager = resourceResolver.adaptTo(AssetManager.class);
        if (assetManager != null) {
            assetManager.createAsset(finalDAMPath, is, contentType, true);
            try {
                addReplicationNodeProperty(finalDAMPath, resourceResolver);
            } catch (RepositoryException e) {
                LOGGER.error("Exception occured while setting property {}", e.getMessage(), e);
            }
            LOGGER.debug("Asset Created at location{}", finalDAMPath);
            if (JobResult.OK == saveSession(session)) {
                // activate the asset
                return replicateAsset(finalDAMPath, session);
            }
        }
        return JobResult.FAILED;
    }

    private void addReplicationNodeProperty(String finalDAMPath, ResourceResolver resourceResolver) throws RepositoryException {
        Resource resource = resourceResolver.getResource(finalDAMPath.concat("/jcr:content/metadata"));
        Node assetNode = resource.adaptTo(Node.class);
        assetNode.setProperty(PWConstants.REPLICATION_PXP,"true");
    }

    /**
     * Replicate the asset
     * 
     * @param assetsLocation
     * @param session
     * @return
     */
    private JobResult replicateAsset(String assetsLocation, Session session) {
        try {
            replicator.replicate(session, ReplicationActionType.ACTIVATE, assetsLocation);
            LOGGER.debug("Asset replicated");
            return JobResult.OK;
        } catch (ReplicationException e) {
            LOGGER.error("Exception while replicating asset {}", e.getMessage(), e);
            return JobResult.FAILED;
        }
    }

    /**
     * Save session.
     * 
     * @param session
     *            the session
     * @return
     */
    private JobResult saveSession(final Session session) {
        if (session.isLive()) {
            try {
                LOGGER.debug("saving session");
                session.save();
                return JobResult.OK;
            } catch (final RepositoryException e) {
                LOGGER.error("Error saving session", e);
            }
        }
        return JobResult.FAILED;

    }

}
