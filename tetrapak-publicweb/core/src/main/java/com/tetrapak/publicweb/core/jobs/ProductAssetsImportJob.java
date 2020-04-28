
package com.tetrapak.publicweb.core.jobs;

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
import com.tetrapak.publicweb.core.services.AssetImportService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;

@Component(immediate = true, service = JobConsumer.class, property = {
		JobConsumer.PROPERTY_TOPICS + "=pxp/dam/assets/create" })
public class ProductAssetsImportJob implements JobConsumer {

	@Reference
	private ResourceResolverFactory resolverFactory;

	@Reference
	private Replicator replicator;

	@Reference
	private AssetImportService assetimportservice;

	private ResourceResolver resourceResolver;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductAssetsImportJob.class);

	public JobResult process(final Job job) {

		Session session = null;
		try {
    		setResourceResolver();
    		if (resourceResolver != null) {
    			session = resourceResolver.adaptTo(Session.class);
    			if (null != session) {
    				String sourceurl = job.getProperty("sourceurl").toString();
    				String finalDAMPath = job.getProperty("finalDAMPath").toString();
    				LOGGER.debug("final DAMPath {}", finalDAMPath);
    
    				// fetch the Asset binary from given URL
    				AssetDetail assetDetail = assetimportservice.getAssetDetailfromInputStream(sourceurl);
    
    				if (null != assetDetail) {
    					// upload the assets in AEM DAM
    					return createAsset(finalDAMPath, assetDetail, session);
    				}
    			}
    		}
    	} finally {
            if (resourceResolver != null && resourceResolver.isLive()) {
                resourceResolver.close();
            }
            if (session != null && session.isLive()) {
                session.logout();
            }
        }
		return JobResult.FAILED;
	}

	/**
	 * 
	 * @param finalDAMPath
	 * @param assetDetail
	 * @param session
	 * @return
	 */
	private JobResult createAsset(String finalDAMPath, AssetDetail assetDetail, Session session) {
		LOGGER.debug("Inside creatin assets {}", finalDAMPath);
		Resource resource = resourceResolver.getResource(finalDAMPath);
		if (resource == null) {
			LOGGER.debug("Asset doesn't exist");
			AssetManager assetManager = resourceResolver.adaptTo(AssetManager.class);
			if (assetManager != null) {
				assetManager.createAsset(finalDAMPath, assetDetail.getIs(), assetDetail.getContentType(), true);
				LOGGER.debug("Asset Created at location{}", finalDAMPath);
				if (JobResult.OK == saveSession(session)) {
					return replicateAsset(finalDAMPath, session); // activate the asset
				}
			}
			return JobResult.FAILED;
		} else {
			LOGGER.info("Asset already exists, skipping Asset creation. asset Path :  {} ", finalDAMPath);
			return JobResult.CANCEL;
		}
	}

	/**
	 * 
	 * @param assetsLocation
	 * @param session
	 * @return
	 */
	private JobResult replicateAsset(String assetsLocation, Session session) {
		try {
			replicator.replicate(session, ReplicationActionType.ACTIVATE, assetsLocation);
			LOGGER.debug("Asset replicated");
			return saveSession(session);
		} catch (ReplicationException e) {
			LOGGER.error("Exception while replicating asset {}", e);
			return JobResult.FAILED;
		}
	}

	/**
	 * Sets the resource resolver.
	 */
	private void setResourceResolver() {
		this.resourceResolver = GlobalUtil.getResourceResolverFromSubService(resolverFactory);
	}

	/**
	 * Save session.
	 * 
	 * @param session the session
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