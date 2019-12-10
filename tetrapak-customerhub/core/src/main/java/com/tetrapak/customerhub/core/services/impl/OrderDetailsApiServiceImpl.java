
package com.tetrapak.customerhub.core.services.impl;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.OrderDetailsApiService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import com.tetrapak.customerhub.core.utils.HttpUtil;
import org.apache.commons.lang3.StringUtils;
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
        String url;
        if (StringUtils.equalsIgnoreCase("parts", orderType)) {
            url = apigeeService.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR +
                    GlobalUtil.getSelectedApiMapping(apigeeService, "orderdetails-parts") + "?order-number=" + orderNumber;
        } else {
            url = apigeeService.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR +
                    GlobalUtil.getSelectedApiMapping(apigeeService, "orderdetails-packmat") + "?order-number=" + orderNumber;
        }

        return HttpUtil.getJsonObject(token, jsonResponse, url);
    }
}
