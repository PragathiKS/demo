package com.tetrapak.publicweb.core.services;

import com.day.cq.replication.ReplicationResult;
import com.day.cq.replication.ReplicationTransaction;
import com.day.cq.replication.TransportContext;

/**
 * CDN Cache Invalidation Service class
 */
public interface CDNCacheInvalidationService {

    /**
     * Send purge request to ServiceInsight via a POST request
     *
     * ServiceInsight will respond with a 201 HTTP status code if the purge request was successfully submitted.
     *
     * @param ctx
     *            - Transport Context
     * @param tx
     *            - Replication Transaction
     * @return ReplicationResult - OK if 201 response from ServiceInsight
     */
    ReplicationResult doActivate(TransportContext ctx, ReplicationTransaction tx);

}
