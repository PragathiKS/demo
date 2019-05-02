package com.tetrapak.commons.core.services.impl;

import com.day.cq.replication.Agent;
import com.day.cq.replication.AgentManager;
import com.tetrapak.commons.core.services.DispatcherFlushService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Service to flush the content from Dispatcher
 * Takes handler[page]; Fetches Configured Dispatcher_hosts at run time.
 *
 * @author Nitin Kumar
 */
@Component(immediate = true, service = DispatcherFlushService.class)
public class DispatcherFlushServiceImpl implements DispatcherFlushService {

    @Reference
    private AgentManager agentManager;

    private String[] hostArray = ArrayUtils.EMPTY_STRING_ARRAY;

    private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherFlushServiceImpl.class);
    private static final String CQ_ACTION_DELETE = "Delete";

    @Override
    public void flush(String dispatcherHandle) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        Map<String, Agent> agents = agentManager.getAgents();
        if (!agents.isEmpty()) {
            for (Map.Entry<String, Agent> agentEntry : agents.entrySet()) {
                if (agentEntry.getValue().isCacheInvalidator() && agentEntry.getValue().isEnabled()) {
                    hostArray = ArrayUtils.addAll(hostArray, agentEntry.getValue().getConfiguration().getAllTransportURIs());
                }
                if (ArrayUtils.isNotEmpty(hostArray)) {
                    flush(dispatcherHandle, httpClient);
                } else {
                    LOGGER.error("DispatcherFlushService: No Active Dispatcher Flush Agents are configured. DispatcherHandle is {}", dispatcherHandle);
                }
            }
        } else {
            LOGGER.error("DispatcherFlushService: No Flush Agents are configured. DispatcherHandle is {}", dispatcherHandle);
        }
    }

    private void flush(String dispatcherHandle, HttpClient httpClient) {
        for (String dispatcherHostURL : hostArray) {
            flushDispatcher(httpClient, dispatcherHandle, dispatcherHostURL);
        }
    }

    private void flushDispatcher(HttpClient httpClient, String dispatcherHandle, String dispatcherHostURL) {
        HttpPost httpPost = new HttpPost();
        try {
            httpPost.setURI(new URI(dispatcherHostURL));
            httpPost.setHeader("CQ-Action:", CQ_ACTION_DELETE);
            httpPost.setHeader("CQ-Handle:", dispatcherHandle);
            httpPost.setHeader("CQ-Path:", dispatcherHandle);

            LOGGER.debug("DispatcherFlushService: : dispatcherHostURL is: {} and dispatcherHandle is: {}", dispatcherHostURL, dispatcherHandle);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                LOGGER.debug("Dispatcher Cache flushed successfully. {}", dispatcherHostURL);
            } else {
                LOGGER.warn("Dispatcher Cache could not be flushed. {}", dispatcherHostURL);
            }
        } catch (URISyntaxException e) {
            LOGGER.error("DispatcherFlushServiceImpl | URISyntaxException: {}", e);
        } catch (ClientProtocolException e) {
            LOGGER.error("ClientProtocolException in DispatcherFlushServiceImpl {}", e);
        } catch (IOException e) {
            LOGGER.error("IOException in DispatcherFlushServiceImpl {}", e);
        } finally {
            httpPost.releaseConnection();
        }
    }
}
