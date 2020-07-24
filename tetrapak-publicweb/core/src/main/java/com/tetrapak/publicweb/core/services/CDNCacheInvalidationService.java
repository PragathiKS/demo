package com.tetrapak.publicweb.core.services;

import org.apache.sling.api.resource.ResourceResolverFactory;

import com.day.cq.replication.ReplicationResult;
import com.day.cq.replication.ReplicationTransaction;
import com.day.cq.replication.TransportContext;

/**
 * CDN Cache Invalidation Service class
 */
public interface CDNCacheInvalidationService {

	/**
	 * Send test request to service insight via a GET request.
	 *
	 * service insight will respond with a 200 HTTP status code if the request was
	 * successfully submitted. The response will have information about the queue
	 * length, but we're simply interested in the fact that the request was
	 * authenticated.
	 *
	 * @param ctx Transport Context
	 * @param tx  Replication Transaction
	 * @return ReplicationResult OK if 200 response from ServiceInsight
	 */
	ReplicationResult doTest(TransportContext ctx, ReplicationTransaction tx);

	/**
	 * Send purge request to ServiceInsight via a POST request
	 *
	 * ServiceInsight will respond with a 201 HTTP status code if the purge request
	 * was successfully submitted.
	 *
	 * @param ctx - Transport Context
	 * @param tx  - Replication Transaction
	 * @return ReplicationResult - OK if 201 response from ServiceInsight
	 */
	ReplicationResult doActivate(TransportContext ctx, ReplicationTransaction tx,
			ResourceResolverFactory resolverFactory);


}

