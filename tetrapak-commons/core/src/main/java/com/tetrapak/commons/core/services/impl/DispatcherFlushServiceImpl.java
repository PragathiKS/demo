package com.tetrapak.commons.core.services.impl;

import com.day.cq.replication.Agent;
import com.day.cq.replication.AgentManager;
import com.tetrapak.commons.core.services.DispatcherFlushService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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
 * Takes handler Fetches Configured Dispatcher_hosts at run time.
 *
 * @author Nitin Kumar
 */
@Component(immediate = true, service = DispatcherFlushService.class)
public class DispatcherFlushServiceImpl implements DispatcherFlushService {

    @Reference
    private AgentManager agentManager;

    private String[] hostArray = ArrayUtils.EMPTY_STRING_ARRAY;

    private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherFlushServiceImpl.class);

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
        HttpGet httpGet = new HttpGet();
        try {
            httpGet.setURI(new URI(dispatcherHostURL));
            httpGet.setHeader("CQ-Action", "Activate");
            httpGet.setHeader("CQ-Handle", dispatcherHandle);
            httpGet.setHeader("CQ-Path", dispatcherHandle);
            httpGet.setHeader("Host", "flush");

            LOGGER.debug("DispatcherFlushService: : dispatcherHostURL is: {} and dispatcherHandle is: {}", dispatcherHostURL, dispatcherHandle);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                LOGGER.debug("Dispatcher Cache flushed successfully. {} for the path {}", dispatcherHostURL, dispatcherHandle);
            } else {
                LOGGER.warn("Dispatcher Cache could not be flushed. {} for the path {}", dispatcherHostURL, dispatcherHandle);
            }
        } catch (URISyntaxException e) {
            LOGGER.error("DispatcherFlushServiceImpl | URISyntaxException: {}", e);
        } catch (ClientProtocolException e) {
            LOGGER.error("ClientProtocolException in DispatcherFlushServiceImpl {}", e);
        } catch (IOException e) {
            LOGGER.error("IOException in DispatcherFlushServiceImpl {}", e);
        } finally {
            httpGet.releaseConnection();
        }
    }
}
