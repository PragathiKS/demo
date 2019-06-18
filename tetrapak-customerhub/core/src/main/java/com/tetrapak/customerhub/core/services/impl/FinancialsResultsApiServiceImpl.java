
package com.tetrapak.customerhub.core.services.impl;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.FinancialsResultsApiService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import com.tetrapak.customerhub.core.utils.HttpUtil;

import java.io.IOException;

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

/**
 * Impl class for Financials Results Service
 */
@Component(immediate = true, service = FinancialsResultsApiService.class, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class FinancialsResultsApiServiceImpl implements FinancialsResultsApiService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FinancialsResultsApiServiceImpl.class);

	@Reference
	private APIGEEService apigeeService;

	@Override
	public JsonObject getFinancialsResults(String status, String documentType, String invoiceDateFrom,
			String customerkey, String token) {
		JsonObject jsonResponse = new JsonObject();
		final String url = apigeeService.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR
				+ GlobalUtil.getSelectedApiMapping(apigeeService, "financialstatement-results") + "?status=" + status
				+ "&document-type=" + documentType + "&invoicedate-from=" + invoiceDateFrom + "&customerkey="
				+ customerkey;

		return HttpUtil.getJsonObject(token, jsonResponse, url);
	}

	@Override
	public HttpResponse getFinancialsInvoice(String documentNumber, String token) {

		final String url = apigeeService.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR
				+ GlobalUtil.getSelectedApiMapping(apigeeService, "financialstatement-invoice") + "/" + documentNumber;

		HttpGet getRequest = new HttpGet(url);
		getRequest.addHeader("Authorization", "Bearer " + token);
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse httpResponse = null;

		try {
			httpResponse = httpClient.execute(getRequest);
			LOGGER.debug("Http Post request status code: {}", httpResponse.getStatusLine().getStatusCode());
		} catch (IOException e) {
			LOGGER.error("IOException occured while getting invoice details response", e);
		}
		return httpResponse;
	}

}
