
package com.tetrapak.customerhub.core.services.impl;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.FinancialsResultsApiService;
import com.tetrapak.customerhub.core.utils.HttpUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;

/**
 * Impl class for Financials Results Service
 */
@Component(immediate = true, service = FinancialsResultsApiService.class,
        configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class FinancialsResultsApiServiceImpl implements FinancialsResultsApiService {

    @Reference
    private APIGEEService apigeeService;

    @Override
    public JsonObject getFinancialsResults(String status, String documentType, String invoiceDateFrom,
                                           String customerkey, String token) {
        JsonObject jsonResponse = new JsonObject();
        final String url = apigeeService.getApigeeServiceUrl() + "/financials/results?status=" + status
                + "&document-type=" + documentType + "&invoicedate-from=" + invoiceDateFrom + "&customerkey="
                + customerkey;

        return HttpUtil.getJsonObject(token, jsonResponse, url);
    }

}
