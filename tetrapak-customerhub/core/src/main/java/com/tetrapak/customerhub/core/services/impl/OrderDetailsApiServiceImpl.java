
package com.tetrapak.customerhub.core.services.impl;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.OrderDetailsApiService;
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
 * Impl class for Order Details Service
 *
 * @author Nitin Kumar
 */
@Component(immediate = true, service = OrderDetailsApiService.class, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class OrderDetailsApiServiceImpl implements OrderDetailsApiService {

    @Reference
    private APIGEEService apigeeService;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDetailsApiServiceImpl.class);

    @Override
    public JsonObject getOrderDetails(String orderNumber, String token, String orderType) {
        JsonObject jsonResponse = new JsonObject();
        final String url = apigeeService.getApigeeServiceUrl() + "/orders/details/" + orderType + "?order-number=" + orderNumber;

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
