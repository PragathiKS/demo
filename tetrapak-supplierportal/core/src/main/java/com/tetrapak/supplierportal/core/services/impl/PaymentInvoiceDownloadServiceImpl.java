package com.tetrapak.supplierportal.core.services.impl;

import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.ACCEPT;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.APPLICATION_JSON;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.APPLICATION_URL_ENCODED;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.AUTHORIZATION;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.BEARER;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.CONTENT_TYPE;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.supplierportal.core.services.PaymentInvoiceDownloadService;

/**
 * Impl class for Payment Invoice Download Service
 * 
 * @author Sunil Kumar Yadav
 */
@Component(immediate = true, service = PaymentInvoiceDownloadService.class)
//@Designate(ocd = APIGEEServiceConfig.class)
public class PaymentInvoiceDownloadServiceImpl implements PaymentInvoiceDownloadService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentInvoiceDownloadServiceImpl.class);

	// private APIGEEServiceConfig config;

	/**
	 * activate method
	 * 
	 * @param config API GEE Service configuration
	 */
	// @Activate
	// public void activate(APIGEEServiceConfig config) {
	// this.config = config;
	// }

	private HttpGet createRequest(final String apiURL, final String authTokenStr, final String documentReferenceId)
			throws UnsupportedEncodingException, URISyntaxException {
		HttpGet getRequest = new HttpGet(apiURL);
		getRequest.addHeader(AUTHORIZATION, BEARER + authTokenStr);
		getRequest.addHeader(CONTENT_TYPE, APPLICATION_URL_ENCODED);
		getRequest.addHeader(ACCEPT, APPLICATION_JSON);
		URI uri = new URIBuilder(getRequest.getURI()).addParameter("fromdatetime", "2023-07-01T00:00:00")
				.addParameter("todatetime", "2023-07-30T00:00:00")
				.addParameter("documentreferenceid", documentReferenceId).build();
		((HttpRequestBase) getRequest).setURI(uri);
		return getRequest;
	}

	@Override
	public HttpResponse preparePdf(String authTokenStr, String documentRef) throws IOException, URISyntaxException {
		LOGGER.debug("PaymentInvoiceDownloadServiceImpl#preparePdf-- Start");
		final String apiURL = "https://api-dev.tetrapak.com/supplierpayments/invoices";
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet getRequest = createRequest(apiURL, authTokenStr, documentRef);
		return httpClient.execute(getRequest);
	}
}
