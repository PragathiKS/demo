
package com.tetrapak.customerhub.core.services.impl;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.FinancialsResultsApiService;
import com.tetrapak.customerhub.core.utils.HttpUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Impl class for Financials Results Service
 */
@Component(immediate = true, service = FinancialsResultsApiService.class,
        configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class FinancialsResultsApiServiceImpl implements FinancialsResultsApiService {
    
    @Reference
    private APIGEEService apigeeService;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FinancialsResultsApiServiceImpl.class);
    
    @Override
    public JsonObject getFinancialsResults(String status, String documentType, String invoiceDateFrom,
            String customerkey, String token) {
        JsonObject jsonResponse = new JsonObject();
        final String url = apigeeService.getApigeeServiceUrl() + "/financials/results?status=" + status
                + "&document-type=" + documentType + "&invoicedate-from=" + invoiceDateFrom + "&customerkey="
                + customerkey;
        
        HttpGet getRequest = new HttpGet(url);
        getRequest.addHeader("Authorization", "Bearer " + token);
        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpResponse httpResponse = httpClient.execute(getRequest);
            LOGGER.debug("Http Post request status code: {}", httpResponse.getStatusLine().getStatusCode());
            jsonResponse = HttpUtil.setJsonResponse(jsonResponse, httpResponse);
        } catch (ClientProtocolException e) {
            LOGGER.error("ClientProtocolException in OrderDetailsApiServiceImpl {}", e);
        } catch (IOException e) {
            LOGGER.error("IOException in OrderDetailsApiServiceImpl {}", e);
        }
        return jsonResponse;
    }
    
}
