
package com.tetrapak.customerhub.core.services.impl;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.OrderDetailsApiService;
import com.tetrapak.customerhub.core.utils.HttpUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;

/**
 * Impl class for Order Details Service
 *
 * @author Nitin Kumar
 */
@Component(immediate = true, service = OrderDetailsApiService.class, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class OrderDetailsApiServiceImpl implements OrderDetailsApiService {

    @Reference
    private APIGEEService apigeeService;

    @Override
    public JsonObject getOrderDetails(String orderNumber, String token, String orderType) {
        JsonObject jsonResponse = new JsonObject();
        final String url = apigeeService.getApigeeServiceUrl() + "/orders/details/" + orderType + "?order-number=" + orderNumber;

        return HttpUtil.getJsonObject(token, jsonResponse, url);
    }
}
