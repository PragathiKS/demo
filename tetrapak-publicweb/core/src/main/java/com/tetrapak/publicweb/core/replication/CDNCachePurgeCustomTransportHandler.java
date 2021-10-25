package com.tetrapak.publicweb.core.replication;

import java.util.Objects;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.day.cq.replication.AgentConfig;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationResult;
import com.day.cq.replication.ReplicationTransaction;
import com.day.cq.replication.TransportContext;
import com.day.cq.replication.TransportHandler;
import com.tetrapak.publicweb.core.services.CDNCacheInvalidationService;

/**
 * Transport handler to send test and purge requests to service insight and handle responses. The handler sets up basic
 * authentication with the user/pass from the replication agent's transport config and sends a GET request as a test and
 * POST as purge request. A valid test response is 200 while a valid purge response is 201.
 *
 * The Flush agent must be configured with following 4 properties:
 *
 * 1. The transport handler is triggered by setting your replication agent's transport URL's to "serviceinsight://".
 * E.g. - serviceinsight://api.cdnetworks.com/client/v4/zones/{zone-id}/purge_cache
 *
 * 2. User: The X-Auth-Email value of service insight account
 *
 * 3. API Key: X-Auth-Key value of service insight account
 *
 * 4. Test URI : API URI for testing connection
 *
 * The transport handler builds the POST request body in accordance with service insight's REST APIs
 * {@link https://api.cdnetworks.com/#zone-purge-files-by-url/} using the replication agent properties.
 */
@Component(service = TransportHandler.class, name = "CDN-Cache-Purge-Agent", immediate = true)
public class CDNCachePurgeCustomTransportHandler implements TransportHandler {

    /**
     * externalizer
     */
    @Reference
    private CDNCacheInvalidationService cdnCacheInvalidationService;

    /**
     * Protocol for replication agent transport URI that triggers this transport handler.
     */
    private static final String SI_PROTOCOL = "cdn-serviceinsight://";

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean canHandle(final AgentConfig config) {
        final String transportURI = config.getTransportURI();
        if (Objects.nonNull(transportURI)) {
            return transportURI.toLowerCase().startsWith(SI_PROTOCOL);
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ReplicationResult deliver(final TransportContext ctx, final ReplicationTransaction tx) {
        final ReplicationActionType replicationType = tx.getAction().getType();
        if (replicationType == ReplicationActionType.ACTIVATE
                || replicationType == ReplicationActionType.DEACTIVATE) {
            return cdnCacheInvalidationService.doActivate(ctx, tx);
        }
        return ReplicationResult.OK;
    }
}
