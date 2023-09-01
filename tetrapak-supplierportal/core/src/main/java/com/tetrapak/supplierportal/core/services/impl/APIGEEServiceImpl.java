package com.tetrapak.supplierportal.core.services.impl;

import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.ACCEPT;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.APPLICATION_JSON;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.APPLICATION_URL_ENCODED;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.AUTHORIZATION;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.BASIC;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.CONTENT_TYPE;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.GRANT_TYPE;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.GRANT_TYPE_VALUE;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.RESPONSE_STATUS_FAILURE;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.RESPONSE_STATUS_SUCCESS;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.RESULT;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.STATUS;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.STATUS_CODE;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.TOKEN;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.UTF_8;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.AUTH_TOKEN;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.tetrapak.supplierportal.core.services.APIGEEService;
import com.tetrapak.supplierportal.core.services.config.APIGEEServiceConfig;
import com.tetrapak.supplierportal.core.utils.GlobalUtil;
import com.tetrapak.supplierportal.core.utils.HttpUtil;

/**
 * Impl class for API GEE Service
 * @author Sunil Kumar Yadav
 */
@Component(immediate = true, service = APIGEEService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = APIGEEServiceConfig.class)
public class APIGEEServiceImpl implements APIGEEService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(APIGEEServiceImpl.class);

    private APIGEEServiceConfig config;

    /**
     * activate method
     * @param config API GEE Service configuration
     */
    @Activate
    public void activate(APIGEEServiceConfig config) {
        this.config = config;
    }

    @Override
    public String getApigeeServiceUrl() {
        return config.apigeeServiceUrl();
    }
    
    @Override
    public String[] getApiMappings() {
        return config.apiMappings();
    }
    
    private String getApigeeClientID() {
        return config.apigeeClientID();
    }

    
    private String getApigeeClientSecret() {
        return config.apigeeClientSecret();
    }
    
	private HttpPost createRequest(final String apiURL, String accToken) throws UnsupportedEncodingException {
		String authString = getApigeeClientID() + ":" + getApigeeClientSecret();
		String encodedAuthString = Base64.getEncoder().encodeToString(authString.getBytes());

		HttpPost postRequest = new HttpPost(apiURL);
		postRequest.addHeader(AUTHORIZATION, BASIC + encodedAuthString);
		postRequest.addHeader(CONTENT_TYPE, APPLICATION_URL_ENCODED);
		postRequest.addHeader(ACCEPT, APPLICATION_JSON);
		List<NameValuePair> postParameters = new ArrayList<>();
		postParameters.add(new BasicNameValuePair(GRANT_TYPE, GRANT_TYPE_VALUE));
		postParameters.add(new BasicNameValuePair(TOKEN, accToken));
		postRequest.setEntity(new UrlEncodedFormEntity(postParameters, UTF_8));
		return postRequest;
	}
    
    @Override
	public JsonObject retrieveAPIGEEToken(String accToken) throws IOException {
		LOGGER.debug("APIGEEServiceImpl#retrieveAPIGEEToken-- Start");
		JsonObject jsonResponse = new JsonObject();
		final String apiURL = getApigeeServiceUrl() + GlobalUtil.getSelectedApiMapping(this,AUTH_TOKEN );

		HttpClient httpClient = HttpClientBuilder.create().build();
		int statusCode = HttpStatus.SC_INTERNAL_SERVER_ERROR;
		try {
			HttpPost postRequest = createRequest(apiURL, accToken);
			HttpResponse httpResponse = httpClient.execute(postRequest);
			statusCode = httpResponse.getStatusLine().getStatusCode();
			jsonResponse.addProperty(STATUS_CODE, statusCode);
			LOGGER.debug("Http Post request status code: {}", statusCode);
			jsonResponse = HttpUtil.setJsonResponse(jsonResponse, httpResponse);
			jsonResponse.addProperty(STATUS, RESPONSE_STATUS_SUCCESS);
			LOGGER.debug("APIGEE Call is Success");
		} catch (IOException e) {
			LOGGER.error("Unable to connect to the APIGEE Call - url {}", apiURL, e);
			jsonResponse.addProperty(STATUS_CODE, statusCode);
			jsonResponse.addProperty(STATUS, RESPONSE_STATUS_FAILURE);
			jsonResponse.addProperty(RESULT, e.getMessage());
		}
		return jsonResponse;
	}

}
