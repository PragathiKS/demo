package com.tetrapak.customerhub.core.services.impl;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.financials.results.Params;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.FinancialResultsApiService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import com.tetrapak.customerhub.core.utils.HttpUtil;
import org.apache.http.HttpResponse;
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
 * Impl class for Financial Results Service
 *
 * @author Nitin Kumar
 */
@Component(immediate = true, service = FinancialResultsApiService.class, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class FinancialResultsApiServiceImpl implements FinancialResultsApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FinancialResultsApiServiceImpl.class);

    @Reference
    private APIGEEService apigeeService;

    /**
     * @param paramsRequest params
     * @param token         token
     * @return json object
     */
    @Override
    public JsonObject getFinancialResults(Params paramsRequest, String token) {
        JsonObject jsonResponse = new JsonObject();
        final String url = apigeeService.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR + GlobalUtil
                .getSelectedApiMapping(apigeeService, "financialstatement-results") + "?invoice-status="
                + paramsRequest.getStatus().getKey() + "&document-type=" + paramsRequest.getDocumentType().getKey()
                + "&invoicedate-from=" + paramsRequest.getStartDate() + "&invoicedate-to=" + paramsRequest.getEndDate()
                + "&customernumber=" + paramsRequest.getCustomerData().getCustomerNumber()
                + "&document-number=" + paramsRequest.getDocumentNumber();

        return HttpUtil.getJsonObject(token, jsonResponse, url);
    }

    /**
     * @param documentNumber document number
     * @param token          token
     * @return http response
     */
    @Override
    public HttpResponse getFinancialInvoice(String documentNumber, String token) {

        final String url = apigeeService.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR + GlobalUtil
                .getSelectedApiMapping(apigeeService, "financialstatement-invoice") + "/" + documentNumber;

        HttpGet getRequest = new HttpGet(url);
        getRequest.addHeader("Authorization", "Bearer " + token);
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse httpResponse = null;

        try {
            httpResponse = httpClient.execute(getRequest);
            LOGGER.debug("Http Post request status code: {}", httpResponse.getStatusLine().getStatusCode());
        } catch (IOException e) {
            LOGGER.error("IOException occurred while getting invoice details response", e);
        }
        return httpResponse;
    }

}
